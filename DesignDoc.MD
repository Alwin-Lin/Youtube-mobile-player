# Design Documentation
## Software Architecture
![](https://user-images.githubusercontent.com/22556115/95029827-ef37bf80-065f-11eb-8263-456efc5d0d2f.jpg)
## List of functions
### Current PlayVideoActivity
- buildPlayer()
  - Builds a SimpleExoPlayer
- initializePlayer()
  - Recives intent from list and turns it into Uri.
  - Makes MediaItem with Uri.
  - Makes mediaSoruce with MediaItem.
  - Prepares and then starts the SimpleExoPlayer.
- releasePlayer()
  - Sets player to null when called
### Current YoutubeURLHandlerActivity
- getYoutubeDownloadUrl(): 
  - Extracts the given Youtube Url then calls startPlayerActivity()
-startPlayerActivity()
  - Sends intent with the extracted URL to PlayVideoActivity
### Current BitmapOverlayVideoProcessor
- setSurfaceSize()
  - Sets the full and close-up size depending on phone's orientation
  - Adding in the calculations for offsets soon
- setOffset()
  - Sets the adjustable range on the close-up area
- draw()
  - Displays the video bassed on video and phone's orientation
