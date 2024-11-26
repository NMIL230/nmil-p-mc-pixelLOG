# Documentation

PixelLog is a Spigot plugin that captures detailed information about players and their interactions within the Minecraft world. Below is a comprehensive list of all data categories that PixelLog can collect:

---

## Player Observation Data

### 1. **Player Attributes**
- **Health**: Current health level of the player.
- **Hunger**: Current hunger level of the player.

### 2. **Player Location and View**
- **Location**: The player's current position as a vector (`x`, `y`, `z`).
- **View Angles**: The player's current yaw (horizontal rotation) and pitch (vertical rotation).

### 3. **Target Information**
- **Target Block**:
    - Block type (e.g., `stone`, `dirt`).
    - Exact hit location as a vector.
- **Target Entity**:
    - Entity type (e.g., `zombie`, `player`).
    - Exact hit location as a vector.

### 4. **Nearby Entities**
- **List of Nearby Entities**:
    - Types of entities within the observation radius (e.g., `sheep`, `skeleton`).

### 5. **Environment**
- **Biome**: The biome the player is currently in (e.g., `plains`, `forest`).

---

## Inventory and Equipment Data

### 1. **Inventory**
- **Items**: List of all items in the player's inventory.
    - `type`: Item type (e.g., `diamond_sword`, `torch`).
    - `amount`: Quantity of the item.

### 2. **Hotbar**
- **Hotbar Slots**:
    - Slot number (1-9).
    - `type`: Item type in the slot.
    - `amount`: Quantity of the item in the slot.

### 3. **Equipment**
- **Equipped Items**:
    - `helmet`: Type of helmet equipped.
    - `chestplate`: Type of chestplate equipped.
    - `leggings`: Type of leggings equipped.
    - `boots`: Type of boots equipped.
    - `main_hand`: Item held in the main hand.
    - `off_hand`: Item held in the off hand.

---

## Events Captured

### 1. **Block Events**
- **Block Break**:
    - Block type.
    - Block location (`x`, `y`, `z`).
- **Block Damage**:
    - Block type.
    - Block location.
    - Item in hand.
- **Block Place**:
    - Block type.
    - Block location.

### 2. **Player Interaction Events**
- **Interact**:
    - Action type (e.g., `right-click`, `left-click`).
    - Item in hand.
    - Target block or entity details.
- **Interact with Entity**:
    - Entity type (e.g., `villager`, `cow`).

### 3. **Combat Events**
- **Entity Damage by Player**:
    - Target entity type.
    - Damage amount.
- **Player Damaged**:
    - Damage source.
    - Damage amount.

### 4. **Item Events**
- **Item Consume**:
    - Item type.
- **Item Drop**:
    - Dropped item type.
    - Quantity dropped.
- **Item Pickup**:
    - Picked up item type.
    - Quantity picked up.
- **Item Held Change**:
    - Newly selected item type.
    - Quantity in the new slot.

### 5. **Inventory Events**
- **Inventory Click**:
    - Slot number clicked.
    - Item clicked (if any).
- **Item Crafting**:
    - Crafted item type.
    - Quantity crafted.

---

## Example Output Structure

### High-Frequency Log
```json
{
  "type": "HIGH_FREQUENCY_LOG",
  "time": "2024-11-26T22:01:29.032Z",
  "game_tick": 509760,
  "location": {"x": 1.5, "y": 0.0, "z": 49},
  "view": {"yaw": 172.05, "pitch": 0},
  "ray_trace_block": {
    "block_type": "crimson_hyphae",
    "hit_location": {"x": 0.1, "y": 1.62, "z": 39}
  },
  "ray_trace_entities": {
    "entity": "skeleton",
    "hit_location": {"x": 2.0, "y": 1.0, "z": 40}
  }
}
```

### Event Log
```json
{
  "type": "EVENT_LOG",
  "time": "2024-11-26T22:01:29.032Z",
  "event": {
    "event_type": "block_break_event",
    "info": {
      "block_type": "stone",
      "block_location": {"x": 10, "y": 64, "z": 20}
    }
  }
}
```

---

If you need more details or examples, feel free to modify this document further!