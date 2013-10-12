Hacky Android widget that lets you create persistent launcher icons for applications you uninstall and reinstall frequently.

Probably only useful for developers.

TODO:

1. Monitor app un/installs, to update the icons when their intents becomes in/actionable.
1. Corner ring shows an 'extras' menu: app details (for an app), edit details, and existing from-launcher screen. 
1. Do something clever for the icon when the intent isn't a plain main launcher... maybe.
1. Allow per-icon user-selected background colours.
1. Fix out of memory error during lumpy config scrolling.

DONE:

1. Added corner ring to reconfigure the widget - precursor to 'extras' menu.
1. Permit editing of the Intent's URL as well as its title, for extra twiddliness.
1. Pretty!
1. Thumb for rapid scrolling in config
1. Asynchronously update the configuration list
1. Persist title and intents through reboot and reinstall of AppGhoul
1. Persist the icons
1. Long-press config item to edit the title
1. Display an appghoul selected from a list of currently installed apps

(c) 2013 Tim Baverstock.
Feel free to pilfer; if it breaks, you get to keep both pieces.
