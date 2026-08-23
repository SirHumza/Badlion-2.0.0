# Badlion Client (1.8.9)

This is the old Badlion Client — build 2.0.0-v-beta, the last one that was ever open source — repackaged so you can just drop it into PrismLauncher and play. No Badlion launcher, no client account, none of that.

You get the whole classic HUD lineup: capes, scoreboard, ping, FPS counter, keystrokes, CPS, potion status, coordinates, all of it. OptiFine 1.8.8 rides along in the same jar, so shaders and the FPS boost work out of the box too.

## Getting it running

Grab [`badlion.jar` from Releases](https://github.com/SirHumza/Badlion-2.0.0/releases/latest).

There are two jars up there:

- `badlion.jar` — the one you want.
- `badlion-all.jar` — same thing plus every Java dependency crammed in. Only useful if you're running straight from a terminal with no launcher (see below).

In PrismLauncher:

1. Make a vanilla **1.8.9** instance, name it whatever.
2. Right-click it → **Edit Instance** → **Version** tab.
3. Click **Add to Minecraft.jar** (older Prism builds call this *Jar Mods*).
4. Pick `badlion.jar`, save, hit Play.

If the Badlion main menu shows up instead of the normal one, it worked. Options → Video Settings now has the full OptiFine menu too.

One thing worth doing first: give it RAM. Edit Instance → Settings → Java → Memory, set max to at least 3–4 GB. The jar carries every cosmetic texture Badlion ever shipped, and starving it of memory = stutter.

## No launcher? Fine

```bash
java -Djava.library.path=natives -jar badlion-all.jar --username YourName
```

You'll need a `natives/` folder next to the jar with the LWJGL natives for your OS — run `./gradlew unzipNatives` from a source checkout to get them. Honest advice though: just use Prism, it handles all of this for you.

## Building it yourself

You need Java 8 (Temurin works fine).

```bash
git clone https://github.com/SirHumza/Badlion-2.0.0.git
cd Badlion-2.0.0
./gradlew build
```

Both jars land in `build/libs/`. OptiFine 1.8.8 is checked in at `libs/optifine.jar`, so the build is fully self-contained — no extra downloads, works offline.

For dev testing, `./gradlew startGame` boots the client directly. Add `--args='--username Pace'` if you want a specific name.

## What's actually in this repo

```
src/main/java/
├── Start.java                     -- standalone entry point
├── net/minecraft/                 -- decompiled MC 1.8.9
├── net/minecraft/launchwrapper/   -- LaunchWrapper (used by the tweaker)
├── net/minecraftforge/            -- just Forge's event bus, not full Forge
└── net/badlion/client/            -- the actual Badlion code
    ├── Wrapper.java               -- the runtime singleton (mods, capes, anti-cheat heartbeat)
    ├── tweaker/                   -- BadlionTweaker + bytecode transformer (entry point)
    ├── manager/                   -- mod profiles, capes, accounts
    ├── mods/                      -- every individual mod (HUD, FOV, FPS, keystrokes, etc.)
    ├── gui/                       -- main menu, slideout, font renderer
    ├── events/                    -- internal event bus
    ├── config/                    -- config file readers
    ├── thread/                    -- async workers (cape lookups)
    └── util/                      -- helpers (network, color, hashing)

libs/optifine.jar                  -- OptiFine 1.8.8 HD U I7, merged into output jars at build time
```

Want to change how something behaves? `net/badlion/client/` is the only folder you need. Everything else is vanilla MC, OptiFine, or LaunchWrapper.

## How it actually works (briefly)

Prism overlays `badlion.jar` onto vanilla `minecraft.jar` at launch, so both Badlion's modified MC classes and OptiFine's win at classload time. Then `BadlionTweaker` registers a bytecode transformer that hooks Badlion's runtime stuff into the game — cape rendering, scoreboard injection, anti-cheat ping, that kind of thing. OptiFine boots through its own tweaker the same way.

## When things break

- **`No lwjgl64 in java.library.path`** — launch with `./gradlew startGame` (it sets the path), or pass `-Djava.library.path=jars/natives` yourself.
- **Game runs but no Badlion UI** — `badlion.jar` goes in *Jar Mods*, not the Forge *Mods* tab. They look similar, they are not the same.
- **Crash on launch: `ClassNotFoundException: net.minecraft.launchwrapper.*`** — you ran the thin jar standalone. Use `badlion-all.jar`, or run through a launcher.
- **Online auth fails** — whatever launches the game has to pass a real `--accessToken`. Prism does this automatically for online accounts; offline accounts skip auth entirely.

## Credit

- Original Badlion team — the client is closed source now; this came from a leaked dump of v2.0.0-v-beta.
- Decompile + repackage: [@SirHumza](https://github.com/SirHumza)
- OptiFine: sp614x
- Minecraft: Mojang
