# Contributing to the project
First of all, thanks a lot for taking the time to contribute! :tada:

The following is a set of guidelines for contributing to this project. 
These are just guidelines, not rules: use your common sense and feel free to propose any changes to this document :grin:

## How can I contribute?
### Reporting bugs
You can report any bugs that you find in the project. In order to do so, you can follow these simple steps:
* Check the [currently filed bugs][search-bug-reports] to see if the bug you found is already reported. If the bug has already been reported:
    * If it's not closed, add the new information that you can provide there.
    * If it's closed, create a new one and reference the closed issue.
* If the bug has not been reported yet, create a new issue using the `Bug report` option. 
* Remember to add all the information required by the issue template to allow us to better pinpoint the problem.

### Suggesting enhancements
You can also suggest changes or new functionality that could be interesting to add to the project.

The steps to request enhancements are pretty similar to the ones used when reporting bugs, which are:
* Check the [currently filed enhancements][search-enhancements] to see if the enhancement you want to suggest is already reported. 
    * If it has already been reported in an issue, upvote and/or comment in it so we can see the demand of said enhancement.
* If the enhancement has not been reported yet, create a new issue using the `Enhancement` option. 
* Remember to add all the information required by the issue template to allow us to better understand the enhancement that you want added in the project.

### Adding new functionality
If you feel that you can contribute to the project with bug fixes or new enhancements by coding them yourself, that's great!! (In fact we encourage you to do it :grin:!)
Don't forget to follow these steps to have your contribution considered by the maintainers of the project:

* Fork the project and create a new branch in your fork with the new code.
* Ensure that the code you write solves the issue you picked and that it follows the [style guide](#Style-guide).
* After you've checked that the code is good to go, submit a PR to the `develop` branch so we can take a look at it!

### Adding translations
If you want to translate the project to your native language, you can do it too!
The steps are pretty simple:

* We use [PoEditor][poeditor-page] to manage translations, so first create an account there.
* Join the project with [this link][poeditor-join-link].
* Choose if you want to complete translations for unfinished languages or if you want to translate the project to a new language.
    * Don't forget to follow the placeholder syntax defined in the [PoEditor plugin that the app uses][poeditor-plugin-placeholders].
* After creating or updating the translations in PoEditor, you can:
    * Create an issue to notify about new translations so the maintainers can sync them to the main branch
    * Create a PR with the new translations
        * Fork the project.
        * Add your PoEditor `apiToken` to `/poeditor.properties`.
        * Create a new branch for the translations and run this command in console in the root of the project: `./gradlew importPoEditorStrings`.
        * Create a PR with the new synced translations.

## Style guide
### Git commit messages
* Use the present tense ("Add feature", not "Added feature")
* Use the imperative mood ("Move cursor to...", not "Moves cursor to...")
* Reference issues and pull requests liberally after the first line

### Kotlin style guide
* A project style for IntelliJ/Android Studio users is embedded in the project, so ensure that the code you develop follows it.
* Install the git hooks that the project provides via `./gradlew installGitHooks` so commit messages and code hooks can be run.
* Detekt will be run whenever you push changes to the repository. That way you don't need to remember to constantly check it whenever you change stuff in the code!
* No hungarian notation!

[search-bug-reports]:https://github.com/adriangl/pict2cam/labels/bug
[search-enhancements]:https://github.com/adriangl/pict2cam/labels/enhancement
[poeditor-page]:https://www.poeditor.com
[poeditor-join-link]:https://poeditor.com/join/project/yGKahGPrHb
[poeditor-plugin-placeholders]:https://github.com/bq/poeditor-android-gradle-plugin#handling-placeholders