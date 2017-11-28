# Zalmoxis2D

Zalmoxis2D is the Java framework for old ActionScript3/OpenFL developers. It is build on top of LibGDX and offers GPU accelerated interfaces for great performance and cross-platform compatibility. 

It is WIP and currently not fully usable. Feel free to help extend Zalmoxis2D.

### FAQ
**1. Is this lybrary ready?**
No. It is Work IN Progress and there are still many things that need to be done. Feel free to help if you have the knowledge.

**2. Is this a direct port of AS3/OpenFL API?**
No. Some features are not worth porting sicne they have better alternatives already in LibGDX that are just as easy to use. Also, there are other features that were not needed in AS3, but are natural under LibGDX.

**3. Can I use this library for comercial purposes?**
Yes! Please do and if you have the time, send me a link to see what you used it for.

**4. Isn't it better to just use LibGDX?**
Yes it is, but, at least for me, the API is harder. 

**5. Can I use Zalmoxis2D to build 3D games?**
No. This only handles 2D functionality. I do plan of having Zalmoxis3D one day, but don't know when.

**6. How do I use this library?**
	a. Download the sources. 
    b. Create a new project using LibGDX installer
	c. Include everything under `core/src` (the `com.zalmoxis2d` package) in your project's `core/src`
    d. Delete the Zalmoxis2D.java file (it is just for testing purposes).
    e. In your main file, under `create()` method, init the stage using `Stage.getInstance().init()`.
    f. In your main file, under `render()` method, call the stage render method
    g. Add display objects to the stage just like you do in AS3, OpenFL
    
**7. Can I contribute to the project?**
Yes, please do. There is still a lot of work that needs to be done so if you can lend a hand, I would greatly appreciate it.


![LibGDX](http://www.badlogicgames.com/wordpress/wp-content/uploads/2011/05/libGDX-RedGlossyNoReflection.png)
![Apache License](https://www.apache.org/img/asf_logo.png)