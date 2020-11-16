# Design Documentation
## Software Architecture
[CUJ](https://github.com/Alwin-Lin/Youtube-mobile-player/blob/main/README.md#cuj)

![](https://user-images.githubusercontent.com/22556115/97812716-0c988300-1c38-11eb-8e17-40813dcea985.jpg)

### PlaceHolder for User-List interaction
![](https://user-images.githubusercontent.com/22556115/99206126-756f1780-276f-11eb-8afa-2b0a9020f29c.jpg)

### For PlayList

![](https://user-images.githubusercontent.com/22556115/98471920-52a79680-21a4-11eb-843f-2ba2041e1e48.jpg)

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
  
