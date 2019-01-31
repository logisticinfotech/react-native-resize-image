<p align="left">
  <a href="https://www.logisticinfotech.com/blog/react-native-import-resized-image-photo-gallery"><img alt="npm version" src="https://img.shields.io/badge/npm-v4.0.0.0-green.svg"></a>
  <a href="https://www.logisticinfotech.com/blog/react-native-import-resized-image-photo-gallery"<><img src="https://img.shields.io/badge/license-MIT-orange.svg"></a>
</p>


## Introduction

Generally its not a good way to use actually image fetched from Photo Gallery or camera to your mobile application specially while you are dealing with number of images. Because as number of images being fetch with actual size provided by Image-picker or camera will lead to memory issue and that makes app to crash as memory stack grows.

I come to accros this issue while developing one of my application using React Native. I have tried to used react-native-photos-framework  to fetch image reference urls instead of image it self but if I load image with that reference url directly then it will lead to memory issue as I have explain above. So now I need something that will provide me resized image with better quality image. I have tried this code to achieve that but there is issue with photo framework and its won’t work.

So i have created Native module with my React Native project. [Here](https://www.logisticinfotech.com/blog/react-native-import-resized-image-photo-gallery) you will get step by step detail how to acheive this.
