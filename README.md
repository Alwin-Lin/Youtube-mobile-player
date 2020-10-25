# Youtube-mobile-player MVP 1
This player is a portotype of [Smart Content Composer](https://www.tdcommons.org/dpubs_series/3670/). It improves video viewing experince on mobile phone by providing 2 seperate views: full and close-up.

## Installing
1. Go to Menu > Settings > Security > and check Unknown Sources to allow your phone to install apps from sources other than the Google Play Store
2. Download the APK from [Releases](https://github.com/Alwin-Lin/Youtube-mobile-player/releases)
3. Once the APK is downloaded, tap on the APK to install

## Screenshots 
### Playing an Youtube video
![](https://user-images.githubusercontent.com/22556115/96386132-794a5280-114d-11eb-966d-2184cf72aa0a.png)

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

## Features:
- As a user, I want to play Youtube video on mobile phone.
  1. Find a video in Youtube App > [Share] > [Player_Protype]
  2. Change the close-up view position by tapping on the full view.
- As a user, I want to play a video from the list
  1. Launch Player_Prototype > select any URL
  2. Change the close-up view position by tapping on the full view
- As a user, I want to add a new video to the playlist
  1. Launch Player_Prototype > Paste video URL > [Add values to listView]

## Known Issues
- The video playback will restart when the phone is rotated.
- Tap twice to exit if a Youtube video is being played.
- When changeing close-up view positions, tap on the upper part of the full view to adjust, there is an invisble but functional controle bar present at the lower part of the screen. 
- Youtube video close-ups may be a bit pixelated, to be investgated further.

## Design Documentation
### Software Architecture
![](https://user-images.githubusercontent.com/22556115/95029827-ef37bf80-065f-11eb-8263-456efc5d0d2f.jpg)
### List of functions
#### Current PlayVideoActivity
- buildPlayer()
  - Builds a SimpleExoPlayer
- initializePlayer()
  - Recives intent from list and turns it into Uri.
  - Makes MediaItem with Uri.
  - Makes mediaSoruce with MediaItem.
  - Prepares and then starts the SimpleExoPlayer.
- releasePlayer()
  - Sets player to null when called
#### Current YoutubeURLHandlerActivity
- getYoutubeDownloadUrl(): 
  - Extracts the given Youtube Url then calls startPlayerActivity()
-startPlayerActivity()
  - Sends intent with the extracted URL to PlayVideoActivity
#### Current BitmapOverlayVideoProcessor
- setSurfaceSize()
  - Sets the full and close-up size depending on phone's orientation
  - Adding in the calculations for offsets soon
- setOffset()
  - Sets the adjustable range on the close-up area
- draw()
  - Displays the video bassed on video and phone's orientation
## Development guide
- WIP
