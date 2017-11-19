# future-app

Run iOS app:
react-native run-ios > /dev/null

To use figwheel type:
re-natal use-figwheel
lein figwheel ios

Reload the app in simulator (⌘ + R)

At the REPL prompt type this:
(in-ns 'future-app.ios.core)

Changes you make via the REPL or by changing your .cljs files should appear live.

Try this command as an example:
(dispatch [:set-greeting "Hello Native World!"])

