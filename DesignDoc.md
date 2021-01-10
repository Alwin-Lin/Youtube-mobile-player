# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

The current app can be devided into five main sections, video data, data storage, video list, and list ineractions. 
 - Video data
    - Defines the Video class
 - Data storage
    - Holds database and the interaciton with it. E.g. delete(), getAll()
 - Video list
    - Takes user inputs, converts into video data, then stores into Data storage.
 - List ineractions (WIP)
    - Controles the interface for adding a video.
    
## Logs
Set Locat filter to info, and the whole process should all be there

## List of functions
### MainActivity
- Handles intent coming from Youtube and sends bundle to VideoEntryDialogFragment
### Notifier
- Builds the list items
### PlayVideoFragment
- Placeholder, going to be replaced with a working videoPlayer
### Video
- Data class, contains the following
  - id 
  - name 
  - description
### VideoEntryDialogFragment
- Set up the small popup window once the floating action button is clicked
- Adds intent to list
### VideoEntryViewModel
- Interacts with LiveData<>
### VideoList
- Sets up the list view on entry, sets onClickListener to floating button
### VideoListAdapter
- Defines what goes into the the list
  - videoId
  - nameView
  - description
  - thumbnail
### ViewListModle
  - Handels delettion of items
  
## Continious building and continuious testing
### What is it?
A process using Google Cloud Platform to automaticly test and build new commits, outputs built APK and test reports.
### What would I need?
A google account
Credit card for 
### How 
