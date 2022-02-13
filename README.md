# FaceDetection

FaceDetection is a simple Android app that detects faces on the selected picture, counts them and establishes for each person wheather he is an adult or a child.

User is able to take a new photo or to select the existing one from the device's gallery. Application uses external API to receive information about the selected picture. According to this information, application presents photo with detected faces surrounded by the rectangles. If the person looks at least 16 years old, rectangle of the face is green and if the detected face looks younger than 16 - its rectangle is blue. Above the photo, user can see the number of the people and how many adults and children were detected.

![face_detection_1](https://user-images.githubusercontent.com/56269299/153762593-b0432c70-74d9-470e-85a4-691d051213d5.png)

Project is based on the data received from [skybiometry.com](https://skybiometry.com). It allows to detect face on the photo and to get information like estimated age and coordinates of the detected face.

Skybiometry API requires image URL in order to return requested data. For this reason, application uses [api.imgbb.com](https://api.imgbb.com), that provides URL address of the given image converted to base64.

Application was built in Kotlin, according to the MVVM architecture pattern. It uses Retrofit2 in order to fetch data from API. Pictures in the application are displayed thanks to Glide.
<br/><br/>

image source: [pixabay.com](https://pixabay.com)
