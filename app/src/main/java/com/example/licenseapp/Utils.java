package com.example.licenseapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    private Context context;
    private static Utils instance;
    private SharedPreferences sharedPreferences;
    private static final String MY_DEVICE_IMG_URL = "my_device_img_url";

    private Utils(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("image_url_link", Context.MODE_PRIVATE);
    }

    public void initData() {
        List<Device> devices = new ArrayList<>();

        initDevices(devices);

        //For loading each the image url for each device in the array
        //We use AtomicInteger in order to avoid any issue counting the number of devices(with uploaded image urls) in this multi threaded environment
        AtomicInteger imageLoadedCount = new AtomicInteger(0);

        for (Device device : devices) {
            scrapeImages(device.getDeviceTitle(), imageUrl -> {
                device.setImgUrl(imageUrl);

                // Increment the loaded image count
                int count = imageLoadedCount.incrementAndGet();

                // Check if all images are loaded
                if (count == devices.size()) {
                    // All images loaded, persist the updated data
                    initDevicesDatabase(devices);
                }
            });
        }

        //For loading the image url for my device
        scrapeImages(Build.BRAND + " " + Build.MODEL, imageUrl -> {
            persistMyDeviceImageUrl(imageUrl);
        });
    }

    private void initDevices(List<Device> devices){
        devices.add(new Device(
                2600,
                "Samsung",
                "Galaxy S6 Edge",
                "The Samsung Galaxy S6 Edge features a 2,600 mAh battery, a unique dual-edge display, and a powerful camera for a stylish smartphone experience",
                "The Samsung Galaxy S6 Edge, a distinctive flagship device, harmonizes cutting-edge technology with elegant design. Its unique dual-edge 5.1-inch Quad HD Super AMOLED display sets it apart, providing not only an immersive visual experience but also convenient access to notifications and shortcuts.\n" +
                        "\n" +
                        "Equipped with a 16 MP rear camera with optical image stabilization and an f/1.9 aperture, the Galaxy S6 Edge excels in capturing vivid and sharp photos. The 5 MP front camera complements this, delivering impressive selfies and facilitating high-quality video calls.\n" +
                        "\n" +
                        "The device is powered by a 2,600 mAh battery, designed to offer sufficient power for daily use. It supports fast charging, enabling users to quickly replenish the battery when needed. The wireless charging feature adds convenience to the charging experience.\n" +
                        "\n" +
                        "Underneath the stylish exterior, the Galaxy S6 Edge boasts a high-performing processor, ensuring smooth multitasking and responsive performance. Running on the Android operating system with Samsung's customizations, the device provides a user-friendly interface and access to a wide range of applications.\n" +
                        "\n" +
                        "The Galaxy S6 Edge combines aesthetics with practicality, featuring a premium glass and metal build. Its dual-edge display not only enhances the visual appeal but also adds functionality. The device incorporates a fingerprint sensor for secure authentication and supports features like heart rate monitoring.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S6 Edge offers a unique and stylish smartphone experience, bringing together a distinctive design, advanced camera capabilities, efficient performance, and practical features."
        ));
        devices.add(new Device(
                3600,
                "Samsung",
                "Galaxy S7 Edge",
                "The Samsung Galaxy S7 Edge features a 3,600 mAh battery, a distinctive curved-edge display, and a powerful camera for a premium experience",
                "The Samsung Galaxy S7 Edge, an exemplary flagship device, showcases a harmonious blend of innovative design and cutting-edge features. Its standout characteristic is the dual-edge 5.5-inch Quad HD Super AMOLED display, curving gently around the edges, offering not only a unique visual appeal but also providing quick access to favorite apps and notifications.\n" +
                        "\n" +
                        "Equipped with a 12 MP rear camera with an f/1.7 aperture, the Galaxy S7 Edge excels in capturing vivid and detailed photos even in low-light conditions. The dual-pixel technology ensures rapid autofocus for sharp and crisp images. The front-facing 5 MP camera complements this, delivering impressive selfies.\n" +
                        "\n" +
                        "A robust 3,600 mAh battery powers the Galaxy S7 Edge, ensuring extended usage on a single charge. The device supports both fast wired and wireless charging, offering convenience and flexibility for users with varied charging preferences.\n" +
                        "\n" +
                        "Under the hood, the Galaxy S7 Edge boasts a high-performing processor, ensuring smooth multitasking and responsive performance. It runs on the Android operating system with Samsung's customizations, providing a user-friendly interface and access to a wide range of applications.\n" +
                        "\n" +
                        "The Galaxy S7 Edge is designed with practicality in mind, featuring water and dust resistance (IP68), adding an extra layer of durability. The fingerprint sensor enhances security, and the device also incorporates features like an always-on display, providing at-a-glance information without unlocking the phone.\n" +
                        "\n" +
                        "In terms of design, the Galaxy S7 Edge presents a sleek and premium appearance with a glass back and metal frame. The device is available in various colors, allowing users to choose a style that suits their preferences.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S7 Edge offers a holistic smartphone experience, combining a striking design, advanced camera capabilities, efficient performance, and thoughtful features to deliver a device that stands out even in a competitive market."
        ));
        devices.add(new Device(
                3000,
                "Samsung",
                "Galaxy S8",
                "The Samsung Galaxy S8 features a 3,000 mAh battery, a stunning Infinity Display, and a powerful camera for a premium mobile experience",
                "The Samsung Galaxy S8, a flagship device, epitomizes excellence in design and functionality. Its standout feature is the expansive 5.8-inch Quad HD+ Infinity Display, which curves around the edges, delivering vibrant colors and immersive visuals. The device is powered by a high-performing processor, ensuring smooth multitasking and responsive performance.\n" +
                        "\n" +
                        "The Galaxy S8's camera system includes a 12 MP rear camera with an f/1.7 aperture, enabling impressive low-light photography. The front-facing 8 MP camera is equally capable, perfect for capturing detailed selfies. The camera supports features like Selective Focus and Pro mode, providing users with creative control over their shots.\n" +
                        "\n" +
                        "With a 3,000 mAh battery, the Galaxy S8 is designed to offer all-day usage on a single charge. The device supports both fast wired and wireless charging, providing convenience and flexibility for users who need a quick power boost.\n" +
                        "\n" +
                        "Running on the Android operating system with Samsung's customizations, the Galaxy S8 offers a user-friendly interface and access to a vast ecosystem of apps. The device also incorporates features like water and dust resistance (IP68), a fingerprint sensor, and an iris scanner for enhanced security options.\n" +
                        "\n" +
                        "The Galaxy S8 provides ample storage space with 64 GB of internal storage, expandable via a microSD card for those who need additional space for apps, photos, and videos.\n" +
                        "\n" +
                        "In terms of design, the Galaxy S8 features a sleek and futuristic appearance, with a glass front and back, and a metal frame. The absence of physical buttons on the front contributes to a clean and modern aesthetic.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S8 offers a compelling package of cutting-edge features, including a stunning display, advanced camera capabilities, efficient performance, and thoughtful design, making it a flagship device that continues to stand out in the smartphone market."
        ));
        devices.add(new Device(
                3000,
                "Samsung",
                "Galaxy S9",
                "The Samsung Galaxy S9 features a 3,000 mAh battery, a brilliant display, and a powerful camera for a premium smartphone experience",
                "The Samsung Galaxy S9, a flagship device, encapsulates a premium smartphone experience with a blend of cutting-edge features and elegant design. Its 5.8-inch Quad HD+ Super AMOLED display delivers vibrant colors and sharp details, providing an immersive visual experience.\n" +
                        "\n" +
                        "Equipped with a 12 MP rear camera with a variable aperture, the Galaxy S9 excels in capturing stunning photos in various lighting conditions. The camera supports features like Super Speed Dual Pixel technology and Super Slow-Mo video recording, allowing users to unleash their creativity in photography and videography.\n" +
                        "\n" +
                        "Under the hood, the Galaxy S9 is powered by a high-performance processor, ensuring smooth multitasking and responsive performance. The device runs on the Android operating system with Samsung's customizations, providing a user-friendly interface and access to a wide range of applications.\n" +
                        "\n" +
                        "The 3,000 mAh battery is designed to provide ample power for day-to-day usage, and the device supports fast charging for quick replenishment. With 64GB or 128GB of internal storage options, expandable via a microSD card, users have sufficient space for apps, media, and files.\n" +
                        "\n" +
                        "The Galaxy S9 also incorporates features such as IP68 water and dust resistance, stereo speakers tuned by AKG, and a fingerprint sensor for enhanced security. Connectivity options include 4G LTE, Wi-Fi, Bluetooth, and NFC.\n" +
                        "\n" +
                        "In terms of design, the Galaxy S9 showcases a sleek and stylish appearance with a glass back and aluminum frame. The Infinity Display with curved edges and minimal bezels adds to the modern aesthetic.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S9 offers a feature-rich and aesthetically pleasing smartphone experience, encompassing a brilliant display, advanced camera capabilities, efficient performance, and thoughtful design elements."
        ));
        devices.add(new Device(
                3400,
                "Samsung",
                "Galaxy S10",
                "The Samsung Galaxy S10 features a 3,400 mAh battery, a stunning display, and a versatile camera system for a premium smartphone experience",
                "The Samsung Galaxy S10, a flagship smartphone, boasts an impressive array of features within its sleek and premium design. The device showcases a stunning 6.1-inch Dynamic AMOLED display, delivering vibrant colors and sharp details for an immersive visual experience. Powered by a high-performance processor, the Galaxy S10 ensures smooth multitasking, responsiveness, and a seamless user interface.\n" +
                        "\n" +
                        "The standout feature of the Galaxy S10 is its versatile camera system. The triple-camera setup includes a 12 MP wide-angle lens, a 12 MP telephoto lens, and a 16 MP ultrawide lens. This combination enables users to capture a diverse range of scenes, from expansive landscapes to detailed close-ups. The device supports 4K video recording and offers various shooting modes for creative photography.\n" +
                        "\n" +
                        "To support its feature-rich capabilities, the Galaxy S10 is equipped with a 3,400 mAh battery. While this capacity provides ample power for day-to-day usage, the device also features fast charging capabilities, ensuring convenient and efficient recharging.\n" +
                        "\n" +
                        "Running on the Android operating system with Samsung's One UI, the S10 provides a user-friendly interface and access to a wide range of applications. Additionally, it supports various connectivity options, including 4G LTE, ensuring seamless communication and internet access.\n" +
                        "\n" +
                        "The design of the Galaxy S10 reflects a blend of elegance and functionality, featuring a glass front and back with an aluminum frame. The Infinity-O display with a punch-hole front camera contributes to a nearly bezel-less screen, enhancing the device's modern aesthetic.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S10 offers a premium smartphone experience with its stunning display, versatile camera capabilities, and efficient performance, making it a flagship device that caters to the diverse needs of users."
        ));
        devices.add(new Device(
                4000,
                "Samsung",
                "Galaxy S20",
                "The Samsung Galaxy S20 features a 4,000 mAh battery, impressive camera system, and a 6.2-inch display for a premium smartphone experience",
                "The Samsung Galaxy S20, a flagship smartphone, seamlessly integrates cutting-edge features into a sleek and premium design. The device boasts a vibrant 6.2-inch Dynamic AMOLED display, offering immersive visuals with vibrant colors and sharp details. Powered by a high-performance processor, the Galaxy S20 ensures smooth multitasking and responsive performance, catering to the demands of modern users.\n" +
                        "\n" +
                        "The standout feature of the Galaxy S20 is its advanced camera system. The triple-camera setup includes a 12 MP wide-angle lens, a 64 MP telephoto lens with 3x hybrid zoom, and a 12 MP ultrawide lens. This versatile combination allows users to capture stunning photos and videos in various scenarios, from expansive landscapes to detailed close-ups. The camera system also supports 8K video recording for ultra-high-definition content creation.\n" +
                        "\n" +
                        "To support its feature-rich capabilities, the Galaxy S20 is equipped with a 4,000 mAh battery. This substantial battery capacity ensures all-day usage on a single charge and is complemented by fast charging capabilities, both wired and wireless, for quick and convenient recharging.\n" +
                        "\n" +
                        "Running on the Android operating system with Samsung's One UI, the S20 provides a user-friendly interface and access to a wide range of apps. It supports 5G connectivity, delivering faster data speeds and improved network performance where available.\n" +
                        "\n" +
                        "The design of the Galaxy S20 reflects a modern and sophisticated aesthetic, featuring a glass and metal construction with slim bezels for a premium look and feel. The device is available in various colors, allowing users to choose a style that suits their preferences.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S20 is a flagship device that combines a stunning display, powerful camera capabilities, and a robust battery to deliver a top-notch smartphone experience."
        ));
        devices.add(new Device(
                        5000,
                        "Samsung",
                        "Galaxy S20 Ultra",
                        "The Samsung Galaxy S20 Ultra boasts a 5,000 mAh battery, 108 MP camera, and a stunning 6.9-inch display for premium performance",
                        "The Samsung Galaxy S20 Ultra is a flagship smartphone that combines cutting-edge technology with premium features. Its expansive 6.9-inch Dynamic AMOLED display offers a stunning visual experience with vibrant colors and sharp contrasts. The device is powered by a high-performance processor, ensuring smooth multitasking and responsiveness.\n" +
                                "\n" +
                                "The standout feature of the Galaxy S20 Ultra is its sophisticated camera system. The quad-camera setup includes a remarkable 108 MP wide-angle lens, a 48 MP periscope telephoto lens with 100x digital zoom, a 12 MP ultrawide lens, and a DepthVision sensor. This versatile arrangement allows users to capture detailed photos and videos across various scenarios, from sweeping landscapes to distant subjects.\n" +
                                "\n" +
                                "The Galaxy S20 Ultra excels in the realm of video recording, supporting 8K video capture for incredibly sharp and detailed footage. On the front, a 40 MP selfie camera ensures high-quality self-portraits and video calls.\n" +
                                "\n" +
                                "To power these features, the device is equipped with a robust 5,000 mAh battery, providing ample power for a full day of usage. The battery supports fast charging, both wired and wireless, ensuring that users can quickly recharge their device.\n" +
                                "\n" +
                                "Running on the Android operating system with Samsung's One UI, the S20 Ultra offers a user-friendly interface and access to a wide range of apps. It supports 5G connectivity, delivering faster data speeds and improved network performance where available.\n" +
                                "\n" +
                                "Design-wise, the Galaxy S20 Ultra features a sleek and premium build with Gorilla Glass on both the front and back. The metal frame adds durability and a sense of sophistication. The device is available in various colors, allowing users to choose a style that suits their preferences.\n" +
                                "\n" +
                                "In summary, the Samsung Galaxy S20 Ultra stands as a flagship powerhouse, offering a stunning display, advanced camera capabilities, and a robust battery for a premium smartphone experience."));
        devices.add(new Device(
                4500,
                "Samsung",
                "Galaxy S20 FE",
                "The Samsung Galaxy S20 FE combines flagship features with affordability, featuring a 6.5-inch display, triple-camera setup, and a 4,500 mAh battery",
                "The Samsung Galaxy S20 FE, or Fan Edition, is a cost-effective yet feature-rich addition to the S20 series. With a vibrant 6.5-inch Super AMOLED display, it delivers crisp visuals and an immersive viewing experience. The device is powered by a high-performing octa-core processor, ensuring smooth multitasking and responsive performance.\n" +
                        "\n" +
                        "Photography enthusiasts will appreciate the versatile triple-camera system, comprising a 12 MP wide-angle lens, a 12 MP ultrawide lens, and an 8 MP telephoto lens with 3x optical zoom. This setup allows for capturing a wide range of scenes and details, from sweeping landscapes to distant subjects. On the front, a 32 MP selfie camera ensures sharp and detailed self-portraits.\n" +
                        "\n" +
                        "The device is equipped with a generous 4,500 mAh battery, providing ample power to keep you connected throughout the day. Whether you're streaming content, playing games, or simply navigating your daily tasks, the Galaxy S20 FE is designed to meet the demands of modern smartphone usage.\n" +
                        "\n" +
                        "Running on the Android operating system with Samsung's One UI, the S20 FE offers a user-friendly interface and access to a vast ecosystem of apps. Additionally, it supports 5G connectivity, ensuring faster download and streaming speeds where available.\n" +
                        "\n" +
                        "Design-wise, the Galaxy S20 FE features a sleek and modern aesthetic with slim bezels, a centered hole-punch camera, and a glass back. It is available in a variety of vibrant colors to suit different preferences.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S20 FE strikes a balance between affordability and flagship features, making it an attractive choice for users seeking a high-quality smartphone experience without breaking the bank."
        ));

        devices.add(new Device(
                4000,
                "Samsung",
                "Galaxy S21",
                "The Samsung Galaxy S21 boasts a 4,000 mAh battery, powerful performance, and a sleek design for a premium smartphone experience",
                "The Samsung Galaxy S21 is a flagship smartphone that seamlessly integrates cutting-edge features with an elegant design. Boasting a vibrant 6.2-inch Dynamic AMOLED display, it delivers stunning visuals with a high refresh rate for smooth interactions. The device is powered by a robust Exynos 2100 or Snapdragon 888 processor (depending on the region), ensuring swift performance and efficient multitasking.\n" +
                        "\n" +
                        "Capture moments in extraordinary detail with the triple-camera system, featuring a 12 MP wide-angle lens, a 12 MP ultrawide lens, and a 64 MP telephoto lens with 3x hybrid zoom. The versatile camera setup enables users to shoot high-quality photos and videos in various scenarios, from expansive landscapes to detailed close-ups.\n" +
                        "\n" +
                        "The Galaxy S21 also excels in the realm of video recording, supporting 8K video capture for incredibly sharp and detailed footage. On the front, a 10 MP selfie camera ensures crisp and vibrant self-portraits.\n" +
                        "\n" +
                        "With a 4,000 mAh battery, the S21 is designed to keep up with your daily tasks, offering a balance between performance and energy efficiency. The device supports fast charging, wireless charging, and wireless PowerShare, allowing you to charge compatible devices by placing them on the back of the phone.\n" +
                        "\n" +
                        "Running on the latest Android version with Samsung's One UI, the S21 provides a user-friendly interface and access to a wide range of apps. It also features 5G connectivity, delivering faster data speeds and improved network performance where available.\n" +
                        "\n" +
                        "The Galaxy S21 boasts a sleek and durable design with Gorilla Glass Victus on the front and a matte glass back. It comes in various colors, allowing users to choose a style that suits their preferences.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S21 is a flagship smartphone that combines cutting-edge technology, a powerful camera system, and an elegant design, making it a compelling choice for users seeking a premium mobile experience."));
        devices.add(new Device(
                5000,
                "Samsung",
                "Galaxy S21 Ultra",
                "The Samsung Galaxy S21 Ultra features a 5,000 mAh battery, advanced cameras, and a 6.8-inch AMOLED display, delivering a premium flagship experience",
                "The Samsung Galaxy S21 Ultra stands at the pinnacle of smartphone innovation, offering a comprehensive and cutting-edge mobile experience. Its expansive 6.8-inch Dynamic AMOLED display with a Quad HD+ resolution provides an immersive visual experience with vibrant colors and deep contrasts. The high refresh rate ensures smooth scrolling and responsiveness, enhancing the overall usability.\n" +
                        "\n" +
                        "At the heart of the device is a powerful processor, either the Exynos 2100 or Snapdragon 888, depending on the region. This ensures seamless multitasking, smooth app navigation, and high-performance capabilities for demanding tasks and applications.\n" +
                        "\n" +
                        "The standout feature of the Galaxy S21 Ultra is its remarkable camera system. The quad-camera setup includes a 108 MP wide-angle lens, a 12 MP periscope telephoto lens with 10x optical zoom, another 10 MP periscope telephoto lens with 3x optical zoom, and a 12 MP ultrawide lens. This versatile arrangement enables users to capture stunning photos and videos in various scenarios, from detailed close-ups to distant subjects. The device also supports 100x digital zoom for unparalleled magnification capabilities.\n" +
                        "\n" +
                        "On the front, a 40 MP selfie camera ensures high-quality self-portraits and video calls. The camera system supports 8K video recording, providing users with the ability to create professional-quality content directly from their smartphones.\n" +
                        "\n" +
                        "The Galaxy S21 Ultra is equipped with a robust 5,000 mAh battery, ensuring all-day usage without compromise. It supports fast charging, both wired and wireless, allowing users to quickly recharge their device. Additionally, the Wireless PowerShare feature enables the phone to wirelessly charge other compatible devices by simply placing them on the back of the phone.\n" +
                        "\n" +
                        "Running on the latest Android version with Samsung's One UI, the S21 Ultra offers a user-friendly interface and access to a vast ecosystem of apps. It supports 5G connectivity, delivering faster data speeds and improved network performance where available.\n" +
                        "\n" +
                        "Design-wise, the Galaxy S21 Ultra exudes sophistication with its sleek and durable build. The glass back and metal frame contribute to a premium look and feel. The device is available in various colors, allowing users to choose a style that suits their preferences.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy S21 Ultra stands as a technological marvel, combining an exceptional display, powerful performance, advanced camera capabilities, and a robust battery, making it a top-tier flagship smartphone in the market."));
        devices.add(new Device(
                10090,
                "Samsung",
                "Galaxy Tab S7 FE",
                "The Samsung Galaxy Tab S7 FE boasts a massive 10,090mAh battery, ensuring extended use on its immersive display for versatile functionality",
                "The Samsung Galaxy Tab S7 FE stands out in the tablet landscape with its robust features, and the cornerstone of its performance is the impressive 10,090mAh battery. This substantial battery capacity is tailored to provide users with extended usage, ensuring that the tablet remains a reliable companion throughout the day.\n" +
                        "\n" +
                        "The immersive experience is heightened by the expansive display, offering a visually stunning canvas for entertainment, productivity, and creativity. Whether you're engaged in multimedia consumption, multitasking with various apps, or expressing your artistic flair, the large and vibrant screen enhances every aspect of the tablet experience.\n" +
                        "\n" +
                        "The Galaxy Tab S7 FE's versatility is further underscored by its powerful specifications and efficient performance. With a capable processor at its core, the tablet effortlessly handles multitasking, ensuring smooth transitions between applications and responsiveness to user interactions. This capability makes it well-suited for both productivity tasks and entertainment purposes.\n" +
                        "\n" +
                        "The tablet is part of the Galaxy ecosystem, running on the Android operating system with Samsung's user-friendly enhancements. Users can access a wide array of applications, games, and productivity tools from the Google Play Store, while also benefiting from Samsung's unique features and optimizations.\n" +
                        "\n" +
                        "Designed with user convenience in mind, the Galaxy Tab S7 FE is equipped with features such as S Pen support for precise input and creative expression. Additionally, it offers a range of connectivity options, including Wi-Fi and optional LTE, ensuring that users can stay connected and productive wherever they go.\n" +
                        "\n" +
                        "In conclusion, the Samsung Galaxy Tab S7 FE is a powerhouse of a tablet, marked by its substantial 10,090mAh battery, expansive display, versatile performance, and thoughtful features, making it a compelling choice for users seeking a premium tablet experience."
        ));
        devices.add(new Device(
                5100,
                "Samsung",
                "Galaxy Tab A7 Lite",
                "The Samsung Galaxy Tab A7 Lite boasts a 5,100mAh battery, ensuring extended use on its compact and versatile design for on-the-go experiences",
                "The Samsung Galaxy Tab A7 Lite is a compact powerhouse designed to deliver a versatile and immersive tablet experience. At the heart of its functionality lies a robust 5,100mAh battery, a testament to its commitment to providing users with extended usage capabilities. This substantial battery capacity is well-suited for users who are often on the move, ensuring that the tablet can keep up with their dynamic lifestyles.\n" +
                        "\n" +
                        "The Galaxy Tab A7 Lite features a compact design that doesn't compromise on performance. Its streamlined form factor makes it easy to carry and use on the go, while the efficient use of space allows for a large and vibrant display that enhances the overall user experience. Whether you're streaming content, reading, or engaging in productivity tasks, the tablet's display provides a crisp and clear visual canvas.\n" +
                        "\n" +
                        "Underneath its sleek exterior, the tablet houses a capable processor that ensures smooth performance for a variety of tasks. From running apps and games to browsing the web, the Galaxy Tab A7 Lite delivers reliable and responsive performance. The tablet operates on the Android operating system, offering access to a wide array of applications, games, and productivity tools available on the Google Play Store.\n" +
                        "\n" +
                        "The Galaxy Tab A7 Lite also embraces creativity and productivity with its compatibility with accessories like the optional S Pen, providing a precise and intuitive input method for note-taking, drawing, and other creative endeavors.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE, make the Galaxy Tab A7 Lite versatile and well-connected. Users can stay connected, whether it's for online meetings, content streaming, or staying updated on social media.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab A7 Lite, with its 5,100mAh battery, compact design, and versatile features, is tailored to meet the needs of users seeking a reliable and portable tablet experience for both entertainment and productivity on the go."
        ));
        devices.add(new Device(
                5050,
                "Samsung",
                "Galaxy Tab Active3",
                "The Samsung Galaxy Tab Active3 boasts a robust 5,050mAh removable battery, emphasizing durability and extended usage for professionals in challenging environments",
                "The Samsung Galaxy Tab Active3 is purpose-built for professionals operating in challenging environments, placing a strong emphasis on durability, functionality, and extended usage. At the heart of its design is a robust 5,050mAh removable battery, tailored to meet the demands of users who require a reliable power source throughout their workday.\n" +
                        "\n" +
                        "The tablet's durability is a standout feature, evident in its MIL-STD-810H certification and IP68 water and dust resistance. This rugged design ensures the device can withstand various environmental factors, making it suitable for industries such as manufacturing, logistics, and field services.\n" +
                        "\n" +
                        "Beyond its durable construction, the Galaxy Tab Active3 is equipped with features tailored for professional use. The tablet supports S Pen functionality, providing precise and intuitive input for tasks such as note-taking and capturing signatures. The glove-touch mode ensures usability even in challenging conditions, where users may be wearing gloves.\n" +
                        "\n" +
                        "The tablet's versatility extends to its performance capabilities. Powered by an octa-core processor, the Galaxy Tab Active3 ensures efficient multitasking and responsive performance. Running on the Android operating system with Samsung's Knox security platform, the tablet provides a secure and user-friendly interface. It also supports Samsung DeX, allowing users to switch between a tablet and desktop-like experience for enhanced productivity.\n" +
                        "\n" +
                        "Connectivity options, including LTE support, NFC, and pogo pin connectors, make the Galaxy Tab Active3 adaptable to various professional requirements. The tablet's biometric features, such as facial recognition and fingerprint scanning, enhance security and user convenience.\n" +
                        "\n" +
                        "The Galaxy Tab Active3 is not just a rugged device; it's a tool that empowers professionals to stay connected, productive, and secure in demanding work environments. With its emphasis on durability, extended battery life, and purpose-built features, the tablet stands as a reliable companion for those who require a robust and versatile computing solution in the field."
        ));
        devices.add(new Device(
                7040,
                "Samsung",
                "Galaxy Tab S6 Lite",
                "The Samsung Galaxy Tab S6 Lite features a 7,040mAh battery, delivering reliable power for productivity and entertainment on its 10.4-inch display",
                "The Samsung Galaxy Tab S6 Lite stands out as a versatile and user-friendly tablet, and at the core of its capabilities lies a robust 7,040mAh battery. This sizeable battery is engineered to provide users with extended usage, making the tablet a reliable companion for various tasks, from productivity to entertainment.\n" +
                        "\n" +
                        "The tablet's expansive 10.4-inch display enhances the overall user experience, offering a generous canvas for immersive content consumption, creative endeavors, and efficient multitasking. The vivid visuals and responsive touch interactions make it well-suited for a range of activities, from streaming videos to sketching or note-taking.\n" +
                        "\n" +
                        "Underneath its sleek and portable design, the Galaxy Tab S6 Lite houses a capable processor, ensuring smooth performance across various applications and tasks. Running on the Android operating system with Samsung's One UI, the tablet provides a user-friendly interface and access to a vast array of applications through the Google Play Store.\n" +
                        "\n" +
                        "Designed for creativity and productivity, the Galaxy Tab S6 Lite supports the S Pen, offering a precise and intuitive tool for note-taking, sketching, and other creative activities. The tablet's lightweight and portable design make it easy to carry, providing users with flexibility in how and where they use their device.\n" +
                        "\n" +
                        "Audio capabilities are another highlight, with dual speakers tuned by AKG and support for Dolby Atmos, delivering a rich and immersive sound experience to complement the vibrant visuals.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE support, ensure that the Galaxy Tab S6 Lite stays connected wherever users go. The tablet caters to a wide range of users, from students and professionals to those seeking a versatile device for entertainment and creative pursuits.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab S6 Lite is a feature-rich tablet with its 7,040mAh battery, large display, versatile functionality, and S Pen support, making it an ideal choice for users who value a balance of performance and portability in their tablet experience."
        ));
        devices.add(new Device(
                7040,
                "Samsung",
                "Galaxy Tab S6",
                "The Samsung Galaxy Tab S6 boasts a 7,040mAh battery, delivering ample power for productivity and entertainment on its stunning 10.5-inch Super AMOLED display",
                "The Samsung Galaxy Tab S6 stands as a flagship tablet, offering a compelling combination of powerful features and sleek design. At the heart of its capabilities is a substantial 7,040mAh battery, meticulously designed to provide users with extended usage for productivity and entertainment.\n" +
                        "\n" +
                        "The tablet's standout feature is its expansive 10.5-inch Super AMOLED display, which sets a high standard for vivid visuals and sharp details. Whether users are engaged in productivity tasks, streaming content, or creative endeavors, the display enhances the overall tablet experience, providing a vibrant and immersive canvas.\n" +
                        "\n" +
                        "Powered by a high-performance processor, the Galaxy Tab S6 ensures efficient multitasking and responsive performance across a range of applications. Running on the Android operating system with Samsung's One UI enhancements, the tablet offers a user-friendly interface and access to a vast ecosystem of applications through the Google Play Store.\n" +
                        "\n" +
                        "Designed for creativity and productivity, the Galaxy Tab S6 supports the S Pen, providing a precise and intuitive input method for note-taking, drawing, and other creative tasks. The tablet's Book Cover Keyboard further extends its functionality, transforming it into a portable workstation for users who require a physical keyboard for extensive typing.\n" +
                        "\n" +
                        "The audio experience is also a highlight, with quad speakers tuned by AKG and support for Dolby Atmos, delivering an immersive sound experience that complements the stunning visuals.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE support, ensure that the Galaxy Tab S6 stays connected wherever users go. The tablet's slim and lightweight design adds to its portability, making it a versatile device suitable for various use cases.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab S6 offers a premium tablet experience with its substantial battery, impressive display, powerful performance, and versatile features, catering to users who demand excellence in both productivity and entertainment."
        ));
        devices.add(new Device(
                5100,
                "Samsung",
                "Samsung Galaxy Tab A 8.0",
                "The Samsung Galaxy Tab A 8.0 boasts a 5,100mAh battery, offering dependable power for daily use on its compact 8-inch display",
                "The Samsung Galaxy Tab A 8.0 is a versatile and compact tablet designed for daily use, and at the core of its functionality is a 5,100mAh battery. This battery capacity is carefully optimized to provide users with dependable power throughout their daily tasks, from browsing and streaming to productivity applications.\n" +
                        "\n" +
                        "The tablet's compact 8-inch display strikes a balance between portability and usability, offering a convenient size for on-the-go use without sacrificing the visual experience. The display, with its vibrant colors and crisp details, enhances content consumption and productivity tasks, making it suitable for a wide range of activities.\n" +
                        "\n" +
                        "Underneath its sleek design, the Galaxy Tab A 8.0 houses a capable processor that ensures smooth performance across various applications. Running on the Android operating system with Samsung's user-friendly enhancements, the tablet provides access to a vast ecosystem of applications through the Google Play Store.\n" +
                        "\n" +
                        "The Galaxy Tab A 8.0 is designed with user convenience in mind. It is lightweight and easy to carry, making it an ideal companion for travel, commuting, or simply relaxing at home. The tablet supports features like Kids Mode, allowing parents to create a safe and engaging digital environment for their children.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE support, ensure that users stay connected wherever they go. The tablet caters to a diverse audience, from students and families to professionals seeking a portable device for both work and leisure.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab A 8.0 is a reliable and user-friendly tablet with its 5,100mAh battery, compact display, and versatile features, making it a practical choice for those who prioritize portability and daily usability in their tablet experience."
        ));
        devices.add(new Device(
                6150,
                "Samsung",
                "Galaxy Tab A 10.1",
                "The Samsung Galaxy Tab A 10.1 boasts a 6,150mAh battery, delivering dependable power for daily use on its expansive 10.1-inch display",
                "The Samsung Galaxy Tab A 10.1 combines a spacious display with reliable performance, and at the heart of its daily functionality is a 6,150mAh battery. This battery capacity is designed to provide users with consistent power throughout a variety of activities, from multimedia consumption and productivity tasks to gaming and creative endeavors.\n" +
                        "\n" +
                        "The tablet's expansive 10.1-inch display offers a generous canvas for immersive content consumption and efficient multitasking. Whether users are streaming videos, browsing the web, or engaging in creative tasks, the vivid visuals and responsive touch interactions contribute to an enriched tablet experience.\n" +
                        "\n" +
                        "Powered by a capable processor, the Galaxy Tab A 10.1 ensures smooth performance across various applications. Running on the Android operating system with Samsung's enhancements, the tablet provides a user-friendly interface and access to a vast ecosystem of applications through the Google Play Store.\n" +
                        "\n" +
                        "Designed with versatility in mind, the tablet supports features like Kids Mode, allowing parents to create a secure and engaging digital environment for their children. The tablet's design is sleek and portable, making it suitable for a range of scenarios, from entertainment at home to productivity on the go.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE support, ensure that users stay connected wherever they go. The tablet caters to diverse user needs, serving as an entertainment hub, a productivity tool, and a versatile device suitable for various lifestyles.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab A 10.1 stands out with its 6,150mAh battery, expansive display, and versatile functionality, offering users a reliable and enjoyable tablet experience for their daily tasks and entertainment needs."
        ));
        devices.add(new Device(
                7040,
                "Samsung",
                "Galaxy Tab S5e",
                "The Samsung Galaxy Tab S5e boasts a 7,040mAh battery, delivering substantial power for productivity and entertainment on its stunning 10.5-inch Super AMOLED display",
                "The Samsung Galaxy Tab S5e stands as an elegant and powerful tablet, blending a sleek design with impressive features. At the core of its capabilities is a substantial 7,040mAh battery, meticulously designed to provide users with extended usage for a diverse range of tasks.\n" +
                        "\n" +
                        "The tablet's standout feature is its expansive 10.5-inch Super AMOLED display, which sets a high standard for vivid visuals and sharp details. Whether users are engaged in productivity tasks, streaming content, or creative endeavors, the display enhances the overall tablet experience, providing a vibrant and immersive canvas.\n" +
                        "\n" +
                        "The Galaxy Tab S5e's performance is driven by a powerful processor, ensuring efficient multitasking and responsive performance. Running on the Android operating system with Samsung's One UI enhancements, the tablet offers a user-friendly interface and access to a vast ecosystem of applications through the Google Play Store.\n" +
                        "\n" +
                        "Designed for versatility, the Galaxy Tab S5e excels in both entertainment and productivity. Its slim and lightweight design makes it easy to carry, while the integration of the DeX mode allows users to switch between a tablet and desktop-like experience for enhanced productivity.\n" +
                        "\n" +
                        "The tablet supports various audio features, with quad speakers tuned by AKG and support for Dolby Atmos, delivering an immersive sound experience that complements the stunning visuals.\n" +
                        "\n" +
                        "Connectivity options, including Wi-Fi and optional LTE support, ensure that the Galaxy Tab S5e stays connected wherever users go. The tablet caters to a broad audience, from users seeking an entertainment hub to those requiring a reliable and powerful device for productivity on the go.\n" +
                        "\n" +
                        "In summary, the Samsung Galaxy Tab S5e impresses with its 7,040mAh battery, expansive Super AMOLED display, powerful performance, and versatile features, making it an ideal choice for users who prioritize a premium tablet experience."
        ));
    }

    private void initDevicesDatabase(List<Device> devices) {
        DeviceDatabase deviceDatabase = new DeviceDatabase(context);
        boolean initialized = true;

        for(Device d : devices){
            try{
                deviceDatabase.addDevice(d);
            }catch (Exception e){
                initialized = false;
            }
        }

        final boolean isInitialized = initialized;
        final Context appContext = context.getApplicationContext();

        // Display the initialization message on the main UI thread
        new Handler(appContext.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (isInitialized) {
                    Toast.makeText(appContext, "Devices initialized successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(appContext, "Error initializing devices", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void persistMyDeviceImageUrl(String imageUrl){
        // Persist the data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(MY_DEVICE_IMG_URL, gson.toJson(imageUrl));
        editor.apply(); // Use apply() for asynchronous write
    }

    public static Utils getInstance(Context context) {
        if(instance == null)
            instance = new Utils(context);
        return instance;
    }

    public List<Device> getAllDevices(Context context) {
        DeviceDatabase deviceDatabase = new DeviceDatabase(context);
        List<Device> devices = deviceDatabase.getAllDevices();
        return devices;
    }

    public String getMyDeviceImgUrl(){
        Gson gson = new Gson();
        Type type = new TypeToken<String>(){}.getType();
        String imageUrl = gson.fromJson(sharedPreferences.getString(MY_DEVICE_IMG_URL, null), type);
        return imageUrl;
    }

    public Device getdeviceById(int id, Context context){
        //Get the device from SharedPreferences (like a database)
        List<Device> devices = getAllDevices(context);
        if(devices != null){
            for(Device d : devices){
                if(d.getId() == id)
                    return d;
            }
        }
        return null;
    }

    //CALLBACK INTERFACE
    //We are using this to get notified when an image url has been found
    public interface ImageLoadListener {
        void onImageLoadComplete(String imageUrl);
    }

    //We use listener as a parameter along with starting a new thread in order to make this method async
    //When listener.onImageLoadComplete(imageUrl) will be called we will know that the image url has ben successfully selected
    public static void scrapeImages(final String searchQuery, final ImageLoadListener listener) {
            new Thread(() -> {
                ArrayList<String> imageUrls = new ArrayList<>();

                try {
                    String searchUrl = "https://www.google.com/search?q=" + searchQuery + "&tbm=isch";
                    Document doc = Jsoup.connect(searchUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                            .get();

                    Elements imageElements = doc.select("img");
                    for (Element imageElement : imageElements) {
                        String imageUrl = imageElement.attr("src");
                        // Check if the URL is a valid image URL
                        if (imageUrl.startsWith("http")) {
                            imageUrls.add(imageUrl);
                        }
                    }

                    if (!imageUrls.isEmpty() && listener != null) {
                        listener.onImageLoadComplete(imageUrls.get(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
    }
}
