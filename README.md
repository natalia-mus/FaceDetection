# FaceDetection

FaceDetection is a simple Android app that detects faces in the selected picture, counts them and establishes for each person wheather he is an adult or a child. User can choose which information about detected faces he wants to present on the picture. Application also works as a simple image converter - it allows to pixelate image or convert it to black and white.
<br/><br/>

User is able to take a new photo or to select the existing one from the device's gallery. Application uses external API to receive information about the selected picture. According to this, application presents photo and information about number of people and how many adults and children were detected.

Once user choose option "detect faces" all detected faces on the photo becomes surrounded by the rectangles. If the person looks at least 16 years old, rectangle of the face is green and if the detected face looks younger than 16 - its rectangle is blue. User can also see other information recieved from API - like estimated age or possible gender (male/female).

<p align="center">
<img src="https://user-images.githubusercontent.com/56269299/163679793-04417105-7b61-4062-85b0-97ea0dac1fec.gif" width="60%" height="60%"/>
</p>

Application allows to make simple image convertions, like pixelization or grayscale. In order to process image, application converts bitmap into the array where each pixel is a RGB object.

<p align="center">
<img src="https://user-images.githubusercontent.com/56269299/163679829-14ef3f09-5b5b-45f8-8fb6-df7f17bca5d8.gif" width="60%" height="60%"/>
</p>
<br/><br/>


## Pixelization algorithm

* first, according to the photo dimensions, future pixel size is choosen

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


Project is based on the data received from [skybiometry.com](https://skybiometry.com). It allows to detect face on the photo and to get information like estimated age, possible gender and coordinates of the detected face.

Skybiometry API requires image URL in order to return requested data. For this reason, application uses [api.imgbb.com](https://api.imgbb.com), that provides URL address of the given image converted to base64.

Application was built in Kotlin, according to the MVVM architecture pattern. It uses Retrofit2 in order to fetch data from API. Pictures in the application are displayed thanks to Glide.


<br/><br/>

image source: [pixabay.com](https://pixabay.com)

<br/><br/>

## Author

[Natalia Muskała](https://github.com/natalia-mus)
