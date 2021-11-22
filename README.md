# KittyGram

Simple application using 
- Architecture Components 
- Retrofit 2.9.0
- Moshi 1.12.0
- Room 2.3.0
- Glide 4.12.0

## Main design concept

The main activity is the one with the whole list of cats fetched from the https://cataas.com open API 
(these cats are not persistently stored in the database, only fetched every time from the API), there is a 
search bar on top, which can re-fetch the cats from the API with the tag entered in the search bar. In
the options the user can choose to see the possible tags.

The user can browse the cats, and can choose cats from this page with the save button on the fragment
to save to a collection, which opens up the Kitty saver fragment. Once a cat is chosen for saving into
collection, if the Kitty is already in the Collection, a SnackBar with the information comes up.
If not, a Kitty entity will be created for it and will be persistently saved (but the picture is still 
retrieved from the API by Glide). On a simple click on the picture, the cat viewer fragment comes up.

An other activity is the collection list activity, in which there's a Fab which opens up the Collection
creator fragment.

The latter activities fragments open up the other important activity, the collection viewer activity 
which lists the cats in the collection. The user can re-rate the cats with the star button on the fragment, 
delete them from the collection with the trash button. On a simple click on the picture, the cat viewer 
fragment comes up.
