import org.jsoup.Jsoup

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: java -jar <jarfile> <eventid>")
        return
    }

    val eventid = args[0]
    val url = "https://msptpoker.com/pastevents/Eventdetails.aspx?EventID=$eventid"
    val doc = Jsoup.connect(url).get()

    // Find the number of entries
    val entriesElement = doc.selectFirst("span:contains(Entrants)")
    val numEntries = entriesElement?.text()?.replace(",", "")?.split(" ")?.get(0)?.toInt()
    println("Number of entries: $numEntries")

    // Calculate the prize pool
    val prizePool = numEntries?.times(970)?.minus(1500)
    println("Prize pool: $$prizePool")

    // Add up all the amounts won by players
    var totalAmountWon = 0
    val playerEntries = doc.select("div.res_list.clearfix")
    for (entry in playerEntries) {
        val amountElement = entry.selectFirst("div.amount strong")
        val amountWon = amountElement?.text()?.replace(",", "")?.replace("$", "")?.toInt()
        totalAmountWon += amountWon ?: 0
    }

    // Print the total amount won by players
    println("Total amount won by players: $$totalAmountWon")

    // Compare the total amount won to the prize pool
    when {
        totalAmountWon > prizePool ?: 0 -> println("The total amount won by players is greater than the prize pool.")
        totalAmountWon < prizePool ?: 0 -> println("The total amount won by players is less than the prize pool.")
        else -> println("The total amount won by players is equal to the prize pool.")
    }
}
