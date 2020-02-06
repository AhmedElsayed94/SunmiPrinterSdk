# Sunmi Printer Sdk

An sdk for Sunmi Printer V1 / V2

# Sunmi
Developer documentation : https://docs.sunmi.com

# Description
This sdk for using Sunmi devices printer in the easiest way possible

# Implementation

Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
  
Step 2. Add the dependency

```
	dependencies {
	        implementation 'com.github.AhmedElsayed94:SunmiPrinterSdk:Tag'
	}
  
```

# How To Use

```
    PrintMe printMe =  new PrintMe(this);
    
    // Print a text 
          printMe.sendTextToPrinter("Testing .. PrintMe",18,true,false,2);
          
    // Print an image
          printMe.sendImageToPrinter(
              printMe.convertDrawableToBitmap(getDrawable(R.mipmap.ic_launcher_round),100,100)
      );
      
    // Print a layout view 
    // Note : the layout must be in the activity or fragment layout and you can hide it
    // "sendViewToPrinter" will convert the view to an image then will auto scale it to fit the size of the printer paper
    
         View view = findViewById(R.id.print_me_layout);
         printMe.sendViewToPrinter(view);
         
    // Also you can access the original sunmi sdk to use it as you wish by aidlUtil
    
        printMe.aidlUtil. -> Anything you want .
       
         
```
# Output for the demo app using the sdk

printing a full layout

<img src="https://user-images.githubusercontent.com/37459585/73938305-a7557280-48ef-11ea-8a1f-2f9deb303b12.PNG"  width="250" height="250" /> <img src="https://user-images.githubusercontent.com/37459585/73938333-b89e7f00-48ef-11ea-85f4-3ea1a376357b.jpg"  width="250" height="250" />


# Thank You 
