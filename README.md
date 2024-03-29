# Pict2Cam
![Latest release](https://img.shields.io/github/v/release/adriangl/pict2cam)
## What is this?
Pict2Cam is an Android fake camera app written in Kotlin that you can use to pick images from your device and feed them to an external camera request.
This means that you can provide any image in your device to apps that require you to take photos from a camera app.

<img src="https://raw.githubusercontent.com/adriangl/pict2cam/master/app/src/main/play/listings/en-US/graphics/phone-screenshots/1.png" width="400px"/>        
<img src="https://raw.githubusercontent.com/adriangl/pict2cam/master/app/src/main/play/listings/en-US/graphics/phone-screenshots/4.png" width="400px"/>        

## Requirements
* An Android device, obviously ;)
* Android 5.0+
  * The app won't work in Android 11+ since Google restricted which apps can appear in the camera picker to [only the system default bundled camera app][android-11-google-restrictions].

## Features
The app provides the following features:

* Pick any image from your device when apps want to use a camera app
* Crop picked images before feeding them to the app that requires a camera app

## Downloads
You can get it from [Google Play][google-play-link], [HUAWEI AppGallery][huawei-appgallery-link] or grab the latest stable or bleeding edge release from the [Releases][releases-page] page.

<a href="https://play.google.com/store/apps/details?id=com.adriangl.pict2cam&utm_source=GitHub&pcampaignid=pcampaignidMKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1"><img alt="Get it on Google Play" src="https://i.imgur.com/r79AgnI.png" height="50px"/></a>
<a href="https://appgallery.cloud.huawei.com/ag/n/app/C104247923?channelId=GitHub&id=54a602f892094969860f32b3f7dd2f90&s=1EA9B1B860B459C3736D4C98F6614B300E83DCA7B1CFE365A437782CE2B1A361&detailType=0&v="><img src="https://i.imgur.com/BNLWW1C.png" alt="Explore it on HUAWEI AppGallery" height="50px"></a>

## Contributing
Refer to the [contributing guide](CONTRIBUTING.md) if you want to contribute to the project!

## Attributions
- Some of the vector assets in the project are modified versions of vectors that you can find in the [FreeVector website](https://www.freevector.com). Make sure to check them out!

## License
```
Copyright (C) 2020 Adrián García

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[google-play-link]:https://play.google.com/store/apps/details?id=com.adriangl.pict2cam
[huawei-appgallery-link]:https://appgallery.cloud.huawei.com/ag/n/app/C104247923
[releases-page]:https://github.com/adriangl/pict2cam/releases
[android-11-google-restrictions]:https://developer.android.com/about/versions/11/behavior-changes-11#media-capture
