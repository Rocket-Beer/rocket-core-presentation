# rocket-core-presentation

Core library to manage view module based in MVVM pattern.

## Preconditions
Conect to repository with GitHub credentials located in "local.properties" archive and config maven properties in "settings.gradle"

local.proterties has to contain:
> github.username=*******  
> github.token=*********************

settings.gradle has contain Rocket-Beer connection with maven
~~~
maven {  
    Properties properties = new Properties()  
    properties.load(file('local.properties').newDataInputStream())  
    url "https://maven.pkg.github.com/Rocket-Beer/*"  
    
    credentials {  
        username = properties.getProperty("github.username")  
        password = properties.getProperty("github.token")  
    }  
}
~~~

## Core ViewModel user manual
Core ViewModel package ease coroutines config initializing CoroutineExceptionHandler, CoroutineDispatcher and CoroutineScope.  
Can be manage coroutines and jobs with this package.

### Implement Rocket core-viewmodel package
~~~
implementation "com.rocket.android.core:core-viewmodel:0.0.2-beta"
~~~

### BaseViewModel
~~~
class ExampleViewModel (
    val dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher = dispatcher) {
~~~
Can be initialize the dispatcher parameter with "Dispatcher.IO" via dependency injection

### Coroutines
it will be possible to use Rocket coroutine lauch or async directly on this way.
~~~
launch {
    //Launch Coroutine block content
}
~~~
~~~
async {
    //Async Coroutine block content
}
~~~

## Core Navigation user manual
Core Navigation package ease to use Jetpack navigation using an especific class.

### Implement Rocket core-navigation package
~~~
implementation "com.rocket.android.core:core-navigation:0.0.2-beta"
~~~

### Clase Navigator
~~~
class AppNavigator(
    navigatorLifecycle: NavigatorLifecycle
) : BaseNavigator.AppBaseNavigator(navigatorLifecycle)
~~~
Can be initialize the navigatorLifecycle parameter with "CoreNavigationProvider.instance.provideNavigatorLifecycle" via dependency injection.

#### Rocket navigations
Among other things, it will be possible to:
* Return to previous fragment cleaning current fragment using goBack() method in AppNavigator.
* Navigate to other fragment using "Direction", "Uri" or "destination id" using optional navOptions.
* Control DialogsFragments.
* Manage NavOptions with clearBackStackTo() method.
~~~
fun goToPreviousScreen(){
    goBack()
}
~~~
~~~
fun goToFragment(){
    val navoptions = clearBackStackTo(clear = <Boolean>, to = <destination id>, popUpToInclusive = <Boolean>)
    goTo(
        //"Direction", "Uri" or "destination id",
        navOptions = navoptions
    )
}
~~~

## Core data Map user manual
Core data Map package ease to use Google maps and Petal maps (Huawei, soported until version "com.huawei.hms:maps:5.3.0.300").

### Implement Rocket core-data-map package
~~~
implementation "com.rocket.android.core:core-data-map:0.0.2-beta"
~~~

### CoreMapView
CoreMapView is a custom view component which can config some maps data:
* Markers
* Zoom
* Polygons
* Location
* Style

## Core data Map GMS user manual
Core data Map package ease to use Google maps.

### Implement Rocket core-data-map-gms package
~~~
implementation "com.rocket.android.core:core-data-map-gms:0.0.2-beta"
~~~

## Core data Map HMS user manual
Core data Map package ease to use Petal maps.

### Implement Rocket core-data-map-hms package
~~~
implementation "com.rocket.android.core:core-data-map-hms:0.0.1-alpha01"
~~~

## Packages
core-viewmodel --> 0.0.2-beta  
core-navigation --> 0.0.2-beta
core-data-map --> 0.0.2-beta
core-data-map-gms --> 0.0.2-beta
core-data-map-hms --> 0.0.1-alpha01
