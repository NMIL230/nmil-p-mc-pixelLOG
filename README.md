# PixelLog

PixelLog is a Minecraft Spigot plugin designed to capture player information across any game mode. This includes, but is not limited to, player location, view angles, inventory, and various game events. The plugin is specifically tailored for Spigot version **1.20.4**.

## Features
- **Player Data Logging**: Captures and logs detailed player information.
- **Event Tracking**: Tracks game events like block placement, item drops, and interactions. 
- **Customizable Commands**: Flexible commands to control logging functionality.

---

## Installation

### Spigot Server
PixelLog is compatible with **Spigot 1.20.4**. Other versions are not supported at this time. To set up a Spigot server, follow the detailed instructions provided in the [Spigot Installation Guide](https://www.spigotmc.org/wiki/buildtools/). Make sure to specify the server version as 1.20.4 during setup.

### PixelLog
1. Download the latest release of PixelLog from the [Releases](https://github.com/your-repo/releases) page.
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
We welcome contributions to improve PixelLog! Please see our [CONTRIBUTING.md](https://github.com/your-repo/CONTRIBUTING.md) for details on how to get involved.

---

## License
PixelLog is released under the [TODO]. See the `LICENSE` file for more information.

---

## Support
If you encounter any issues or have feature requests, please open an issue on our [GitHub repository](https://github.com/your-repo/issues).

