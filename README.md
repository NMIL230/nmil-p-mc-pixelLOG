# PixelLog

PixelLog is a Minecraft Spigot plugin designed to capture player information across any game mode. This includes, but is not limited to, player location, views, inventory, and various game events. The plugin is specifically tailored for Spigot version **1.20.4**.

A comprehensive list of all data categories can be captured is provided [here](https://github.com/NMIL230/nmil-mc-pixelLOG/blob/main/DOCUMENTATION.md).

## Features
- **Player Data Logging**: Captures and logs detailed player information.
- **Event Tracking**: Tracks game events like block placement, item drops, and interactions. 
- **[COMING SOON]** We will introduce a configuration files to allow users to set parameters and the type of data collected.

---

## Installation

### Spigot Server
PixelLog is compatible with **Spigot 1.20.4**. Other versions are not supported at this time. To set up a Spigot server, follow the detailed instructions provided in the [Spigot Installation Guide](https://www.spigotmc.org/wiki/buildtools/).

### PixelLog
1. Download the latest release of PixelLog from the [Releases](https://github.com/NMIL230/nmil-mc-pixelLOG/releases) page.
2. Place the `.jar` file in the `plugins` folder located in your Spigot server directory.
3. Start the Spigot server by running the following command:

   ```bash
   ./start.sh
   ```

4. Check the server logs to ensure that PixelLog has been successfully enabled. You should see the message:

   ```
   PixelLOG Enabled
   ```

---

## Usage

PixelLog provides the following commands for managing logs:

### Commands

| Command         | Description                                   | Usage                           |
|-----------------|-----------------------------------------------|---------------------------------|
| **pl-start**    | Start collecting logs on the command sender.  | `/pl-start`                    |
| **pl-stop**     | Stop collecting logs on the command sender.   | `/pl-stop`                     |
| **pl-start-op** | Start collecting logs on an online player (OP only). | `/pl-start-op [username]` |
| **pl-stop-op**  | Stop collecting logs on an online player (OP only).  | `/pl-stop-op [username]`  |
| **pl-version**  | Display the plugin version (test command).    | `/pl-version`                  |

### Logfile
After stopping the log data or when the player exits the game, you can find the generated logfile in the PixelLogs folder under the Spigot directory.

---

## Contribution
We welcome contributions to improve PixelLog! Please see our [CONTRIBUTING.md](https://github.com/NMIL230/nmil-mc-pixelLOG/blob/main/CONTRIBUTING.md) for details on how to get involved.

---

## License
TODO



---

## Support
TODO
