# Design Documentation
## Software Architecture
[CUJ](https://github.com/Alwin-Lin/Youtube-mobile-player/blob/main/README.md#cuj)

![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

### PlaceHolder for User-List interaction
![](https://user-images.githubusercontent.com/22556115/99919255-2bd37f00-2cd1-11eb-9e31-dbee8ea1ca42.jpg)
![](https://user-images.githubusercontent.com/22556115/99919260-2f670600-2cd1-11eb-948d-9f38e32e968e.jpg)
### List and application architecture
#### List
![](https://user-images.githubusercontent.com/22556115/99919259-2e35d900-2cd1-11eb-8851-caf7a74bd2d3.jpg)
#### App
![](https://user-images.githubusercontent.com/22556115/99919257-2d04ac00-2cd1-11eb-96c7-da3db958ccdc.jpg)

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
  
