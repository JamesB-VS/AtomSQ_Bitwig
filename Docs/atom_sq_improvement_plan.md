# Atom SQ Extension Improvement Plan

## Current Status
- Extension works but needs refactoring after 2-year hiatus
- Using Bitwig API 18, Java 20
- Main class `AtomSQExtension.java` is 1000+ lines
- Uses Bitwig framework from `com.bitwig.extensions.framework.*`

## Priority Improvements

### 1. Code Organization & Architecture
**Problem**: Single large class with mixed responsibilities
**Solution**: Break into focused managers
```java
// Target structure:
AtomSQExtension (main)
├── HardwareManager (surface, buttons, encoders)
├── LayerManager (all layer creation/management)
├── BrowserManager (popup browser handling)
├── DisplayManager (screen updates, sysex)
└── ModeManager (mode switching logic)
```

### 2. Variable Naming & Conventions
**Issues**:
- `DisplayMode DM` → `DisplayMode displayMode`
- `HardwareHandler hH` → `HardwareHandler HARDWARE_HANDLER`
- `MidiOut mMidiOut` → `MidiOut midiOut`
- Inconsistent prefix usage (m prefix not standard Java)

### 3. Constants & Magic Numbers
**Replace**:
- Encoder sensitivity: `100`, `127` → `EncoderConfig.SENSITIVITY_NORMAL`
- Array sizes: `6`, `8`, `9` → `LayerCounts.SEND_COUNT` etc.
- SysEx strings → Named constants in `SysExCommands` class

### 4. Error Handling
**Add**:
- Try-catch blocks around initialization
- Proper logging with context
- Graceful degradation on component failure
- User-friendly error notifications

### 5. Code Cleanup
**Remove**:
- All commented debug `println` statements
- Large commented code blocks in `flush()`
- Unused variables and methods
- Test/experimental code

### 6. Method Decomposition
**Target methods** (currently too large):
- `createInstLayer()` → separate setup methods
- `activateLayer()` → simplify logic
- `flush()` → extract update logic
- `init()` → break into focused init methods

### 7. Type Safety Improvements
**Replace string comparisons with enums**:
```java
public enum ControllerMode {
    SONG, SONG2, INST, INST2, INST3, EDIT, USER, BROWSER, INST_EMPTY
}
```

### 8. Constants Classes
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

### 9. Documentation
**Add JavaDoc for**:
- All public methods
- Complex private methods
- Class-level descriptions
- Parameter and return value descriptions

### 10. API Updates
**Check for**:
- Deprecated method usage in API 18
- New API features that could simplify code
- Better alternatives to current approaches

## Implementation Order
1. **Constants extraction** (quick wins)
2. **Code cleanup** (remove commented code)
3. **Variable renaming** (IDE refactor)
4. **Method decomposition** (improve readability)
5. **Class extraction** (architecture improvement)
6. **Error handling** (robustness)
7. **Enum implementation** (type safety)
8. **Documentation** (maintainability)

## Files to Focus On
**Primary**: `AtomSQExtension.java` (main refactoring target)
**Secondary**: `DisplayMode.java` (constants, cleanup)
**Supporting**: `HardwareHandler.java`, `SysexHandler.java` (constants)

## Key Architectural Questions
1. Should layer management be extracted to separate class?
2. How to handle mode switching more elegantly?
3. Should browser functionality be its own manager?
4. Can display updates be made more efficient?

## Specific Code Examples

### Current Issues Examples
```java
// Magic numbers
encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(0, hH.CC_ENCODER_1 + index, 100));

// String-based mode switching
switch (stsname){
    case "Shift":
    case "SamplesBrowser":
    // ...
}

// Large methods
private void createInstLayer() {
    // 50+ lines of mixed concerns
}
```

### Improved Examples
```java
// Named constants
encoder.setAdjustValueMatcher(mMidiIn.createRelativeSignedBitCCValueMatcher(
    0, HARDWARE_HANDLER.CC_ENCODER_1 + index, EncoderConfig.SENSITIVITY_NORMAL));

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
```

## Testing Strategy
1. **Regression testing**: Ensure all current functionality works after changes
2. **Incremental changes**: Make small, testable modifications
3. **Version control**: Commit after each successful improvement
4. **Backup**: Keep working version before major refactoring

## Success Metrics
- **Maintainability**: Code is easier to understand and modify
- **Reliability**: Better error handling and logging
- **Performance**: No degradation in responsiveness
- **Extensibility**: Easier to add new features in the future

This plan provides a systematic approach to improving the codebase while maintaining all existing functionality.