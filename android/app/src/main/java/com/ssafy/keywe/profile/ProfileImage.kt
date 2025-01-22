package com.ssafy.keywe.profile


//@Composable
//fun Profile(
//    modifier: Modifier = Modifier,
//    name: String = "김싸피"
//) {
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data("https://w7.pngwing.com/pngs/710/71/png-transparent-profle-person-profile-user-circle-icons-icon-thumbnail.png")
//                .build(),
//            contentDescription = "ExampleImage",
//            contentScale = ContentScale.Fit,
//            modifier = Modifier
//        )
//        Text(
//            text = name,
//            style = subtitle2,
//            modifier = Modifier
//                .padding(top = 8.dp)
//                .wrapContentSize(),
//            textAlign = TextAlign.Center
//        )
//    }
//}