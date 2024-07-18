# FaceDetection

FaceDetection is a simple Android app that detects faces in the selected picture, counts them and establishes for each person wheather he is an adult or a child. User can choose which information about detected faces he wants to present in the picture. Application also works as a simple image converter - it allows to pixelate image, convert it to black and white, sepia, negative or turn it vertically and horizontally.
<br/><br/>

User is able to take a new photo or to select the existing one from the device's gallery. Application uses external API to receive information about the selected picture. According to this, application presents photo and information about number of people and how many adults and children were detected.

Once user choose option "detect faces" all detected faces in the photo becomes surrounded by the rectangles. If person looks at least 16 years old, rectangle of the face is green and if the detected face looks younger than 16 - its rectangle is blue. User can also see other information recieved from API - like estimated age or possible gender (male/female).

Application allows to make simple image convertions. In order to process image, application converts bitmap into the array where each pixel is a RGB object.

<br/>
<p align="center">
<img src="https://github.com/user-attachments/assets/5f5caac9-0f3e-44d7-ba2f-bd855f33cae5" width="30%" height="30%"/>
</p>
<br/><br/>


## Pixelization algorithm

* first, according to the photo dimensions, future pixel size is chosen

* array of RGBs is divided into the packages of pixels - size of each package depends on the future pixel size

* red, green and blue values of every pixel from the package are added up and then divided by the number of pixels, what gives the average color of pixel after pixelization process

* information about the average color is saved for every pixel in package

* array of new RGB values is converted into bitmap
<br/><br/>
<br/><br/>



<p align="center">
<img src="https://user-images.githubusercontent.com/56269299/163680057-36f52872-9c7b-45ee-bafe-a449bf2bf416.png"/>
<br/>
<i>before pixelization - image 6x6 contains 36 pixels, each of them has different color</i>
<br/><br/>
<br/><br/>


<img src="https://user-images.githubusercontent.com/56269299/163680139-66ab0f56-a8b3-418b-9a9b-8936ce2165ff.png"/>
<br/>
<i>single package, in this case pixel size after the pixelization process equals 3x3</i>
<br/><br/>
<br/><br/>


<img src="https://user-images.githubusercontent.com/56269299/163681200-2214353e-d628-48ff-a466-4c7d40dc51b5.png"/>
<br/>
<i>image still contains 36 pixels but every 9 pixels share the same average color so it looks like it contains only 4 pixels</i>
<br/><br/>
<br/><br/>

</p>
<br/><br/>

## Grayscale algorithm

* RGB values of a single pixel are added up and divided by 3 in order to get average of these values

* result is assigned to red, green and blue so all of them share the same value

* when color has the same values in red, green and blue, color represented by those three values is always between white and black

* array of the new RGB values is converted into bitmap
<br/><br/>
<br/><br/>


<p align="center">
<img src="https://user-images.githubusercontent.com/56269299/163681436-7c3c3882-667c-4fa9-8966-671d58ccf729.png"/>
</p>
<br/><br/>
<br/><br/>


## Sepia algorithm

In order to obtain sepia image, its red, green and blue values need to be recalculated according to the following formula:

      newRed = 0.393 * red + 0.769 * green + 0.189 * blue
      newGreen = 0.349 * red + 0.686 * green + 0.168 * blue
      newBlue = 0.272 * red + 0.534 * green + 0.131 * blue

<br/><br/>
If any of new values is greater than 255, then the result is 255.


<br/><br/>
<br/><br/>


## Image turning algorithm

Picture can be turned both vertically and horizontally. In order to turn image, bitmap is converted into an array of pixels. 

When turning upside down, elements of the arrays representing the next vertical rows of pixels are being reversed. Pixels from the bottom of the image come at the top of it.

<p align="center">
<img src="https://github.com/natalia-mus/FaceDetection/assets/56269299/e2b3a22d-d2d4-40a7-a4c9-c6a4d94c8ec7" width="20%" height="20%"/>
</p>

When turning horizontally (mirroring), elements of arrays representing next horizontal rows of pixels are being reversed. Wherefore, first pixel on the left becomes the first one on the right.

<p align="center">
<img src="https://github.com/natalia-mus/FaceDetection/assets/56269299/0ee3bedd-cae8-424f-b171-8dd987570610" width="30%" height="30%"/>
</p>

<br/><br/>
<br/><br/>


## Negative algorithm

In order to invert colors of the image - red, green and blue values of each pixel must be subtracted from 255.

<br/><br/>
<br/><br/>

## Project details

Project is based on the data received from [skybiometry.com](https://skybiometry.com). It allows to detect face in the photo and to get information like estimated age, possible gender and coordinates of the detected face. API has limited queries - user can monitor API usage from application.

<br/><br/>
<p align="center">
<img src="https://github.com/natalia-mus/FaceDetection/assets/56269299/e18c6b39-2af9-447d-893d-5bbe94fa38ae" width="40%" height="40%"/>
</p>
<br/><br/>

Skybiometry API requires image URL in order to return requested data. For this reason, application uses [api.imgbb.com](https://api.imgbb.com), that provides URL address of the given image converted to base64. User is supposed to have his own imgbb.com account so he can pass own API key to application - then all of the images will upload to user's account. User also can choose whether uploaded images should be deleted within 60 seconds or not.

Application was built in Kotlin, according to the MVVM architecture pattern. It uses Retrofit2 in order to fetch data from API. Pictures in the application are displayed thanks to Glide.


<br/>

image source: [pixabay.com](https://pixabay.com)

<br/>

## Author

[Natalia Muskała](https://github.com/natalia-mus)
