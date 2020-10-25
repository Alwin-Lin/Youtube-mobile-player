# Design Documentation
## Software Architecture
### Prototype 

![](https://user-images.githubusercontent.com/22556115/97121986-a5cb1500-16df-11eb-9efb-335e0c0b0871.jpg)

### Planned

![](https://user-images.githubusercontent.com/22556115/97121985-a5327e80-16df-11eb-94ca-0e3eaab03b4a.jpg)

## CUJ
### Start from App
1. User clicks/adds a video URL
2. URL pass to PlayVideoActivity via intent
3. BitmapOverlayVideoProcessor checks phone and video orentation and displays video.
4. If user taps on the full view, it displays that reagen onto close-up.
5. User finishes video, press return key, goes back to menu.
### Start from sharing in Youtube
1. User shares video and opens with app
2. URL passed to YoutubeURLHandler to be processed, then sent to PlayVideoActivity
3. BitmapOverlayVideoProcessor checks phone and video orentation and displays video.
4. If user taps on the full view, it displays that reagen onto close-up.
5. User finishes video, press return key, goes back to Youtube.
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