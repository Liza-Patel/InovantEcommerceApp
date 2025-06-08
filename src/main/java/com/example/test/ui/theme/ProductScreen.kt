package com.example.test.ui.theme

import com.example.test.viewmodel.ProductViewModel
import com.example.test.data.model.Product

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.core.text.HtmlCompat
import com.example.test.R

@Composable
fun ProductScreen(viewModel: ProductViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val product by viewModel.product.collectAsState()
    var quantity by remember { mutableStateOf(1) }
    var selectedColorIndex by remember { mutableStateOf(0) }

    product?.let { item ->
        val swatchUrls = item.configurable_option.firstOrNull()?.attributes?.map { it.images.firstOrNull() ?: "" } ?: emptyList()
        ProductDetailContent(
            item = item,
            quantity = quantity,
            selectedColorIndex = selectedColorIndex,
            swatchUrls = swatchUrls,
            onQuantityChange = { quantity = it },
            onColorSelected = { selectedColorIndex = it }
        )
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailContent(
    item: Product,
    quantity: Int,
    selectedColorIndex: Int,
    swatchUrls: List<String>,
    onQuantityChange: (Int) -> Unit,
    onColorSelected: (Int) -> Unit
) {
    val formattedPrice = try {
        "%.2f KWD".format(item.final_price.toDouble())
    } catch (e: Exception) {
        "${item.final_price} KWD"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Once Collection Weekly",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* favorite */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                    }
                    IconButton(onClick = { /* share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = { /* cart */ }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ImageCarousel(images = swatchUrls)

            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.brand_name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {

                        Text("SKU: ${item.sku}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Text(formattedPrice, style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
                }

                Spacer(Modifier.height(8.dp))

                Text("Color", style = MaterialTheme.typography.titleSmall)
                ColorSwatches(swatchUrls, selectedColorIndex, onColorSelected)

                Spacer(Modifier.height(8.dp))

                PaymentInfoBox()

                Spacer(Modifier.height(8.dp))

                Text("Quantity", style = MaterialTheme.typography.titleSmall)
                QuantitySelector(quantity, onQuantityChanged = onQuantityChange)

                Spacer(Modifier.height(8.dp))

                ExpandableSection("Product Information", item.description)

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { /* handle add to bag */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add to bag")
                    }
                    OutlinedButton(onClick = { /* handle share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<String>) {
    val pagerState = rememberPagerState { images.size }

    Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                val color = if (pagerState.currentPage == index) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
fun PaymentInfoBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("or 4 interest-free payments", style = MaterialTheme.typography.bodySmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("0.88 KWD", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Learn more", style = MaterialTheme.typography.labelSmall.copy(color = Color.Blue))
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.ic_tabby_logo)
                .build(),
            contentDescription = "Tabby Logo",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun ColorSwatches(colors: List<String>, selectedIndex: Int, onColorSelected: (Int) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(vertical = 8.dp)) {
        itemsIndexed(colors) { idx, url ->
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (selectedIndex == idx) 2.dp else 1.dp,
                        color = if (selectedIndex == idx) Color.Blue else Color.Gray,
                        shape = CircleShape
                    )
                    .clickable { onColorSelected(idx) },
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChanged: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
            onClick = { if (quantity > 1) onQuantityChanged(quantity - 1) },
            enabled = quantity > 1
        ) {
            Icon(Icons.Default.RemoveCircle, contentDescription = "Decrease quantity")
        }
        Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
        IconButton(onClick = { onQuantityChanged(quantity + 1) }) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cleanText = remember(content) {
        HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.weight(1f))
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null
            )
        }
        if (expanded) {
            Text(cleanText, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
        }
    }
}
