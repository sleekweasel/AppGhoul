Hacky Android widget that lets you create persistent launcher icons for applications you uninstall and reinstall frequently.

Probably only useful for developers.

TODO:

1. Monitor app un/installs, to update the icon when an app or intent is gone.
1. Update icon to tombstone/icon when app is un/installed for a plain main launcher.
1. Shortcut to app details screen: Force close, etc. Maybe shift the icon left and have a small settings icon.
1. Do something clever for the icon when the intent isn't a plain main launcher... maybe.
1. Fix out of memory error during lumpy config scrolling.
1. Is it worth curating copies of the icons?

DONE:

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
