# Atom SQ LED Protocol Research

## What's out there

The good news: there **is** more community knowledge now than when you first reverse-engineered your extension. The FL Studio script by `forgery810` on GitHub has done the most thorough LED protocol work.

**No official PreSonus MIDI implementation document exists** for the Atom SQ. Everything is community-discovered.

---

## Piano key LED protocol (the new discovery for you)

Colors are **not done via SysEx** — they use regular MIDI Note On messages, with the **MIDI channel encoding the color**:

| MIDI Status | Channel | Color |
|-------------|---------|-------|
| `0x90` (144) | 0 | White (vel 127) / Off (vel 0) |
| `0x91` (145) | 1 | Blue |
| `0x92` (146) | 2 | Purple |
| `0x93` (147) | 3 | Yellow |

- `data2` (velocity) controls **brightness/shade** — e.g. `0x91` + vel `50` = light blue, vel `0` = full blue
- The device expects you to **send white first** (`0x90, 0, note, 127`), then send the color message — it seems to need the white "base" before a color overlay takes effect

---

## Piano key note numbers (LED addressing)

The 32 keyboard LEDs map to note numbers **36–67**, mirroring standard MIDI note layout:

- **White keys (top row):** `52, 53, 55, 56, 57, 59, 60, 62, 63, 64, 66, 67`
- **Black keys:** `37, 39, 42, 44, 46, 49, 51, 54, 56, 58, 61, 63, 66`
- **Full LED range:** 36–67 (same as the pads — the addressing is shared/mode-dependent)

---

## What's still unknown

- The **full color palette** — only white, blue, purple, yellow, and off are documented. Red, green, orange etc. may exist on higher channel numbers but haven't been mapped publicly.
- Whether the **white-first pattern is always required** or just a quirk of the FL Studio script's implementation
- Whether the **brightness scaling** via data2 is linear or stepped

---

## Practical approach for your extension

To light key #60 (middle C) blue:
```java
// Step 1: set white base
midiOut.sendMidi(0x90, 60, 127);
// Step 2: apply blue
midiOut.sendMidi(0x91, 60, 0);
```

To turn it off:
```java
midiOut.sendMidi(0x90, 60, 0);
```

To explore unknown colors, you'd just iterate `0x94`, `0x95`, `0x96`... on a test note and see what comes out — same brute-force method you used before, but now you have a starting framework.

---

## Sources

- [forgery810/fl-studio-presonus-atom-sq (lights.py)](https://github.com/forgery810/fl-studio-presonus-atom-sq)
- [alt-key-project/Bitwig-extension-Atom-SQ-MIDI-mode](https://github.com/alt-key-project/Bitwig-extension-Atom-SQ-MIDI-mode)
- [KVR Audio - New Atom SQ Extension thread](https://www.kvraudio.com/forum/viewtopic.php?t=595445)
