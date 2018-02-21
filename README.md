# King's Corner
> Classic card game rendered using console graphics

![MIT License](https://img.shields.io/badge/License-MIT-lightgrey.svg?style=for-the-badge)
![Version 1.0.0](https://img.shields.io/badge/Version-1.0.0-lightgrey.svg?style=for-the-badge)
![Stability Stable](https://img.shields.io/badge/Stability-Stable-lightgrey.svg?style=for-the-badge)

<p align="center" >
	<img src="docs/images/animation_1.gif" width="33%" />
	<img src="docs/images/animation_2.gif" width="33%" />
	<img src="docs/images/animation_3.gif" width="33%" />
</p>

## About
I have implemented an extra command for the computer to finish the round for you. This is for debugging and is hidden within the application itself. To activate it type `^` as the command when it is your turn.

## Building & Running
This project uses maven as a build system. Therefore to package this library into a jar, execute `mvn package` while in the project root directory. Since the implementation of the game's graphics involve unicode characters, it is important to append the `-Dfile.encoding=UTF-8` flag when running your Java application.  This will ensure that the unicode characters are rendered correctly in your console. If this flag is not passed, then you will likely see the `?` character in place said unicode characters.

## Bugs / Feature Requests
If you have any feature requests, please open up an issue. Similarly if there are any bugs found, please report them by opening up an issue.  If a bug is found, please include steps to reproduce the issue, alongside the expected and actual behavior.
