# Design Documentation
## Software Architecture
CUJ

![](https://github.com/Alwin-Lin/Youtube-mobile-player/blob/main/README.md#cuj)


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
  
