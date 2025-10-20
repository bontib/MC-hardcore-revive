# Changelog

All notable changes to this project will be documented in this file.

The format is loosely based on "Keep a Changelog" and follows Semantic Versioning.

## [1.1] - 2025-10-20
### Changed
- Make Resurrection Stone crafting recipe configurable via `config.yml`.
  - New config section: `resurrection_stone` with the fields:
    - `enabled` (boolean) — whether the recipe is registered.
    - `shape` (list of 3 strings) — three rows of the crafting grid (each row must be 3 characters long). Use a space for an empty slot.
    - `ingredients` (map) — map a single character (used in `shape`) to a Bukkit `Material` name (e.g. `NETHER_STAR`, `DIAMOND`).
  - Example default snippet (written to `src/main/resources/config.yml` and copied to server `plugins/HardcoreRevive/config.yml` on first run):

```yaml
resurrection_stone:
  enabled: true
  shape:
    - "DGD"
    - "GNG"
    - "DGD"
  ingredients:
    N: "NETHER_STAR"
    G: "GOLD_INGOT"
    D: "DIAMOND"
```

### Implemented
- `RecipeManager` now reads `resurrection_stone` config and registers a `ShapedRecipe` from it. It validates the `shape` and ingredient mappings and logs warnings when falling back to defaults.
- `HardcoreRevive` now calls `saveDefaultConfig()` so the default `config.yml` is created in the plugin data folder and is editable by server admins.
- Safer command registration (null-check around `getCommand("bindrevive")`) to avoid NPEs if the command is missing in `plugin.yml`.

### Behavior / Compatibility Notes
- If the config has invalid material names or missing mappings for characters used in the shape, the plugin will try to fall back to sensible defaults for the known characters (N, G, D). If a character has no mapping and no fallback, recipe registration is aborted and a warning is logged.
- If `resurrection_stone.enabled` is set to `false`, the recipe won't be registered.
- Existing servers upgrading to 1.1 will get the default `config.yml` on first startup. Edit the file under `plugins/HardcoreRevive/config.yml` to customize the recipe.

## [1.0] - initial release
### Added
- Basic HardcoreRevive plugin features:
  - `ResurrectionStone` item utility to create and identify the custom item.
  - `/bindrevive <player>` command to bind a player to a Resurrection Stone.
  - `PlayerDeathListener` and `ItemDropListener` to handle death-related behavior and revival flow.
  - `DeadPlayerStorage` to persist information about dead players.
  - `RecipeManager` (originally registered a hard-coded recipe for the Resurrection Stone).

## Notes
- The project uses PaperMC (`paper-api`) and is packaged with Maven. The current `pom.xml` artifact version is `1.1`.

If you'd like, I can also:
- Add documentation to the `README.md` describing how to edit the recipe in the server `config.yml`.
- Add a small unit-style test (where feasible) or a smoke-check that the recipe registers at runtime (requires a running server environment).
- Add comments in `config.yml` that explain valid `Material` names and give more examples.

---

Generated on 2025-10-20

