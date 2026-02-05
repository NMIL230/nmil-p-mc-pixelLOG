# Data Collection Reference

PixelLOG produces structured JSON datasets that seamlessly integrate both continuous state measurements and discrete event occurrences. This document details the log structure, available data types, and collection parameters.

## Technical Architecture
The framework is implemented as a custom plugin for Spigot Minecraft server (version 1.20.4), using JDK 21 and Maven. While tailored for specific versions, PixelLOG's architecture abstracts version-dependent components, allowing for potential adaptability across different Minecraft environments.

## 1. Log Structure Overview
PixelLOG organizes collected data into structured JSON files, with each file representing a complete player session.

### Root Metadata
The root level contains session identification and temporal boundaries:

```json
{
    "logfile_id": "Unique_Session_Identifier",
    "filename": "Player_UTCTimeString",
    "username": "Player_ID",
    "game_start_time": "Session_Start_UTCTimeString",
    "game_end_time": "Session_End_UTCTimeString",
    "plugin_version": "Version_String"
}

```

### Data Entry Format

The core behavioral data is stored in a chronologically ordered array. There are two primary entry types: **State Logs** (Polled) and **Event Logs** (Triggered).

#### A. Player State Log (High Frequency)

Collected by `PlayerStatePoller` at configurable frequencies (e.g., 20Hz).

```json
{
    "type": "HIGH_FREQUENCY_LOG_20Hz",
    "time": "UTCTimeString",
    "game_tick": "Server_Tick_Count",
    "location": {"x": 0.0, "y": 0.0, "z": 0.0},
    "view": {"pitch": 0.0, "yaw": 0.0},
    "ray_tracing_block": {
        "hit_location": "coordinates",
        "block_type": "material"
    }
}

```

#### B. Event Log (Discrete)

Captured by `PlayerEventListener` when specific actions occur.

```json
{
    "type": "EVENT_LOG",
    "time": "UTCTimeString",
    "game_tick": "Server_Tick_Count",
    "event": "Event_Name",
    "event_info": {
        // Event-specific context data
    }
}

```

---

## 2. Player State Data Specifications

PixelLOG supports comprehensive state collection. The following variables are tracked:

### Spatial & Navigation

* **Location:** Precise world coordinates (x, y, z).
* **View:** Orientation metrics (pitch, yaw) to reconstruct gaze and attentional focus.
* **Ray-Traced Target:** Real-time tracking of line-of-sight interactions (identifies specific blocks or entities being looked at).

### Physiological

* **Health:** Current health level.
* **Hunger:** Current hunger/saturation level.

### Contextual Environment

* **Nearby Blocks & Entities:** Continuous assessment of surroundings within a configurable radius.
* **Biome:** Classification of the current environment.

### Inventory & Equipment

* **Hot Bar:** Items currently in the quick-access toolbar.
* **Inventory:** Complete contents of the player's storage.
* **Equipment:** Armor worn and items held in main/off-hands.

---

## 3. Event Logging Specifications

The system captures specific player-triggered events categorized below:

### 3.1 Block Interactions

| Event Type | Trigger | Captured Data |
| --- | --- | --- |
| **Block Break** | Player breaks a block | `player`, `event`, `block_type`, `block_location` |
| **Block Damage** | Player begins mining | `player`, `event`, `block_type`, `block_location`, `item_in_hand` |
| **Block Place** | Player places a block | `player`, `event`, `block_type`, `block_location` |

### 3.2 Entity Interactions

| Event Type | Trigger | Captured Data |
| --- | --- | --- |
| **Player Interact** | Interaction with world (doors, levers, etc.) | `action`, `item_in_hand`, `block_type`, `block_location` |
| **Player-Entity** | Direct interaction (shearing, feeding) | `entity_type`, `interaction_type` |
| **Entity Damage** | Combat (Dealing or taking damage) | `entity_type`, `damage_amount`, `damage_cause` |

### 3.3 Item Management

| Event Type | Trigger | Captured Data |
| --- | --- | --- |
| **Consumption** | Eating/drinking an item | `item_type` |
| **Item Holding** | Switching hotbar slots | `new_item` |
| **Drop Item** | Throwing an item | `item_type`, `amount` |
| **Pickup Item** | Collecting an item | `item_type`, `amount` |
| **Inventory Click** | Moving items in GUI | `slot`, `clicked_item` |
| **Crafting** | Completing a recipe | `crafted_item`, `amount` |
