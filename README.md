# **Prosa App**
Completed submission for modul "Belajar Aplikasi Android Intermediate" in program "Magang dan studi independen bersertifikat" (MSIB) batch 3 Kampus merdeka X Dicoding Indonesia.

![cover](screenshot/cover.png)
## **Disclaimer ⚠️**
This repository is created for sharing and educational purposes only. Plagiarism is unacceptable and is not my responsibility as the author.

## **Installation**
There are several stages for application installation.
1. Star this repository.
2. Cloning this repository to android studio.
    ```git
    git clone https://github.com/rezacahyono/ProsaApp.git
    ```
3. Create your **Google Maps api key** and **Geoapify key**.

    You can find tutorial get api key [Google Maps Api key](https://www.proweb.co.id/articles/maps/google-maps-api-key.html) and [Geoapify](https://www.geoapify.com/get-started-with-maps-api).
     
    After getting the api key You can create file **api_key.properties** for **Geoapify** like so:
    ```gradle
    geoapify_api_key = PUT_HERE
    ```
    And create file **values api_keys.xml** for **Google api key** like so :
    ```xml
    <string name="GOOGLE_MAP_API_KEY">PUT_HERE</string>
    ```
4. clean and assemble

## **Tech**
+ Navigation Component
+ [Keyboard event listener](https://github.com/yshrsmz/KeyboardVisibilityEvent)
+ [Geoapify](https://www.geoapify.com/maps-api)
+ [Dots Indicator](https://github.com/tommybuonomo/dotsindicator)
+ Camerax
+ [Coil](https://coil-kt.github.io/coil/getting_started/)
+ [Retrofit](https://square.github.io/retrofit/)
+ Datastore
+ etc
