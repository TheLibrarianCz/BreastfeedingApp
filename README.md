# Breastfeeding App

Very simple app written in Compose that first began as a help of a friend which I cloned to be playground for Compose.

## Features

- Record a breastfeeding time and certain additional options
- Shows the time for the next feeding based on the settings
- History and statistics for the last few days
- Export and import data to/from JSON files

## Tech

It  uses a number of open source projects to work properly:

- [Navigation Component] - at least I think so
- [DataStore] - reactive new trendy version of Preferences
- [Gson] - Well, Gson de-serializer thingy.
- [Hilt] - classic Android DI
- [Room] - to store the records and use some of the SQL magic

The use of LocalDate and LocalTime was not exactly to my liking but in a spirit of keeping the logic as is I created workarounds it rather than rework it completely.

## License

MIT

**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[gson]: <https://github.com/google/gson>
[DataStore]: <https://developer.android.com/topic/libraries/architecture/datastore>
[Navigation Component]: <https://developer.android.com/jetpack/compose/navigation>
[Hilt]: <https://developer.android.com/training/dependency-injection/hilt-android>
[Room]: <https://developer.android.com/training/data-storage/room>
[NounProject]: <https://thenounproject.com/>
