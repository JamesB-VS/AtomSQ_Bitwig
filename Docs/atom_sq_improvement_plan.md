# Atom SQ Extension Improvement Plan

## Current Status
- Extension works but needs refactoring after 2-year hiatus
- Using Bitwig API 18, Java 20
- Main class `AtomSQExtension.java` is 1,115 lines
- `DisplayMode.java` is 337 lines with high method duplication
- Uses Bitwig framework from `com.bitwig.extensions.framework.*`

## Priority Improvements

### 1. Code Cleanup
**Remove first to reduce scope of all subsequent steps**:
- All commented debug `println` statements
- Large commented code blocks in `flush()`
- Unused variables and methods
- Test/experimental code
- Triage all `TODO` comments — resolve, implement, or remove (see Known TODOs below)

### 2. Enum-Based Mode Switching
**Replace string comparisons — do this early for type safety during renaming**:
```java
public enum ControllerMode {
    SONG, SONG2, INST, INST2, INST3, EDIT, USER, BROWSER, INST_EMPTY
}
```
Current string-based switching is woven throughout the code; the enum makes all
subsequent refactoring steps safer.

### 3. Constants & Magic Numbers
**Replace**:
- Encoder sensitivity: `100`, `127` → `EncoderConfig.SENSITIVITY_NORMAL`
- Array sizes: `6`, `8`, `9` → `LayerCounts.SEND_COUNT` etc.
- SysEx strings → Named constants in `SysExCommands` class
- Hardcoded MIDI CC values (176, 15–21, 29, 143, etc.) → named constants

```java
public static final class SysExCommands {
    public static final String LIVE_MODE_ON = "F0000106221301F7";
    public static final String DISPLAY_CONFIG = "F0000106221300F7";
    public static final String BUTTON_CONFIG = "F0000106221400F7";
    // etc.
}

public static final class EncoderConfig {
    public static final int SENSITIVITY_NORMAL = 100;
    public static final int SENSITIVITY_STEPPED = 127;
    public static final int COUNT = 9;
}

public static final class LayerCounts {
    public static final int SEND_COUNT = 6;
    public static final int REMOTE_CONTROL_COUNT = 8;
    public static final int DEVICE_BANK_SIZE = 3;
    public static final int TRACK_BANK_SIZE = 3;
}
```

### 4. Variable Naming & Conventions
**Issues**:
- `DisplayMode DM` → `DisplayMode displayMode`
- `HardwareHandler hH` → `HardwareHandler hardwareHandler`
- `MidiOut mMidiOut` → `MidiOut midiOut`
- Inconsistent prefix usage (m prefix not standard Java)

### 5. Method Decomposition
**Target methods** (currently too large):
- `createInstLayer()` → separate setup methods
- `activateLayer()` → simplify logic
- `flush()` → extract update logic
- `init()` → break into focused init methods

```java
// Before
private void createInstLayer() {
    // 50+ lines of mixed concerns
}

// After
private void createInstLayer() {
    setupInstLayerLights();
    setupInstLayerButtons();
    setupInstLayerEncoders();
}
```

### 6. DisplayMode Refactor (High Priority — Equal to God-Class Split)
**Problem**: 13+ near-identical mode methods (`SongMode()`, `Song2Mode()`, `InstMode()`,
`InstEmptyMode()`, `Inst2Mode()`, `Inst3Mode()`, `EditMode()`, `UserMode()`, etc.).
Each repeats the same SysexBuilder patterns and nested loops with hardcoded values.

**Solution**: Replace parallel methods with a data-driven approach:
```java
// Map each ControllerMode to a DisplayConfig record/object
private static final Map<ControllerMode, DisplayConfig> DISPLAY_CONFIGS = Map.of(
    ControllerMode.SONG,  new DisplayConfig(...),
    ControllerMode.INST,  new DisplayConfig(...),
    // etc.
);

private void applyDisplayMode(ControllerMode mode) {
    DisplayConfig config = DISPLAY_CONFIGS.get(mode);
    // single implementation driven by config
}
```

### 7. Code Organization & Architecture
**Problem**: Single large class with mixed responsibilities
**Solution**: Break into focused managers

```java
// Target structure:
AtomSQExtension (main)
├── HardwareManager (surface, buttons, encoders)
├── LayerManager (all layer creation/management)  ← highest value extraction
├── BrowserManager (popup browser handling)       ← distinct open/close lifecycle
├── DisplayManager (screen updates, sysex)
└── ModeManager (mode switching logic)
```

**Architectural decisions** (resolved):
- **Extract layer management?** Yes — biggest win after the god-class split
- **Mode switching?** Enum approach (item #2) covers this
- **Browser as own manager?** Yes — popup open/close lifecycle doesn't belong in the main class
- **Display update efficiency?** Covered by the data-driven DisplayMode refactor (item #6)

### 8. Logging
**Replace** scattered debug `println` calls with structured `host.println()` logging
at key init steps. This is far more useful than try-catch wrappers for a Bitwig extension,
since init failures are generally fatal and the host already captures the stack trace.

**Do not** add try-catch blocks around initialization — they swallow stack traces without
providing recovery options in this context.

### 9. Documentation
**Add comments only where the WHY is non-obvious**:
- SysEx quirks and hardware-specific MIDI CC mappings
- Non-obvious invariants in layer activation logic
- Workarounds for specific Bitwig API behaviour

Full JavaDoc is low ROI for a personal project. Comment the surprising parts as you touch them.

### 10. API Updates
**Check for**:
- Deprecated method usage in API 18
- New API features that could simplify code
- Better alternatives to current approaches

## Known TODOs (Triage Required)
The following are unresolved `TODO` comments found in the code. Each should be resolved,
implemented, or removed during the cleanup step:
- Platform detection (Windows/Mac/Linux) — not yet implemented
- Display initialization issues — not resolved
- Panel identification improvements — noted as possible
- Code flagged as "ugly and manual" — needs cleanup

## Implementation Order
1. **Code cleanup** — remove dead code, triage TODOs (reduces scope for everything else)
2. **Enum implementation** — type safety before renaming touches string comparisons
3. **Constants extraction** — quick wins
4. **Variable renaming** — IDE refactor with type safety in place
5. **Method decomposition** — improve readability
6. **DisplayMode refactor** — eliminate the 13-method duplication
7. **Class extraction** — full architecture improvement
8. **Logging** — structured host.println at init points
9. **Documentation** — comment surprising parts as you go

## Files to Focus On
**Primary**: `AtomSQExtension.java` (main refactoring target, 1,115 lines)
**Equal priority**: `DisplayMode.java` (337 lines, 13+ duplicated mode methods)
**Supporting**: `HardwareHandler.java`, `SysexHandler.java` (constants)

## Specific Code Examples

### Current Issues
```java
// Magic numbers
encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(0, hH.CC_ENCODER_1 + index, 100));

// String-based mode switching
switch (stsname){
    case "Shift":
    case "SamplesBrowser":
    // ...
}

// Large methods with mixed concerns
private void createInstLayer() {
    // 50+ lines of mixed concerns
}

// 13 near-identical display methods
public void SongMode() { /* SysexBuilder + loops */ }
public void Song2Mode() { /* SysexBuilder + loops */ }
public void InstMode() { /* SysexBuilder + loops */ }
// ...repeated 10 more times
```

### Improved Examples
```java
// Named constants
encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(
    0, hardwareHandler.CC_ENCODER_1 + index, EncoderConfig.SENSITIVITY_NORMAL));

// Enum-based switching
switch (currentMode) {
    case SHIFT, SAMPLES_BROWSER -> { /* continue */ }
    default -> lastLayer = activeLayer;
}

// Decomposed methods
private void createInstLayer() {
    setupInstLayerLights();
    setupInstLayerButtons();
    setupInstLayerEncoders();
}

// Data-driven display modes
private void applyDisplayMode(ControllerMode mode) {
    DisplayConfig config = DISPLAY_CONFIGS.get(mode);
    // single implementation
}
```

## Testing Strategy
1. **Regression testing**: Ensure all current functionality works after changes
2. **Incremental changes**: Make small, testable modifications
3. **Version control**: Commit after each successful improvement step
4. **Backup**: Keep working version before major refactoring

## Success Metrics
- **Maintainability**: Code is easier to understand and modify
- **Reliability**: Structured logging; no swallowed exceptions
- **Performance**: No degradation in responsiveness
- **Extensibility**: Easier to add new features in the future
