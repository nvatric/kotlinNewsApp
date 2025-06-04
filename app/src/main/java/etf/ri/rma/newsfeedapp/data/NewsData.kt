package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem(
                uuid = "f1dcac63-4b67-4485-ad92-1348761dba05",
                title = "Super League: Wigan trounce Salford as Jacob Douglas scores hat-trick",
                snippet = "Watch highlights as Wigan's Jacob Douglas scores a hat-trick in a dominant Super League win against Salford.",
                imageUrl = "https://ichef.bbci.co.uk/ace/standard/1024/cpsprodpb/692b/live/9f52aa90-3e0c-11f0-b6e6-4ddb91039da1.png",
                category = "sports",
                isFeatured = false,
                source = "bbc.co.uk",
                publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
            NewsItem(
                uuid = "ce1c523b-39fc-4ec2-88cc-f574d42add77",
                title = "Shohei Ohtani hits 2 HRs as Dodgers top Aaron Judge, Yankees",
                snippet = "Shohei Ohtani homered twice as the Dodgers overcame an Aaron Judge homer and a four-run deficit to beat the Yankees.",
                imageUrl = "https://a3.espncdn.com/combiner/i?img=%2Fphoto%2F2025%2F0531%2Fr1500621_1296x729_16%2D9.jpg",
                category = "sports",
                isFeatured = false,
                source = "espn.com",
                publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
            NewsItem(
                uuid = "807f3766-9a22-4624-ad48-b15e2f565921",
                title = "Scotland: Amy Rodgers sustains broken jaw, Charlotte Newsham called up as replacement",
                snippet = "Midfielder Amy Rodgers is ruled out of Scotland's final Nations League A group game against the Netherlands after breaking her jaw.",
                imageUrl = "https://ichef.bbci.co.uk/ace/branded_sport/1200/cpsprodpb/e7a6/live/e6ad8f80-3e08-11f0-9c86-919ba9abf877.jpg",
                category = "sports",
                isFeatured = false,
                source = "bbc.co.uk",
                publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
            NewsItem(
                uuid = "f1a07f25-db09-406e-9531-ad79694c39cc",
                title = "Trump Responds to Potential Sean 'Diddy' Combs Pardon",
                snippet = "President Donald Trump on Friday did not rule out pardoning Sean “Diddy” Combs if the music mogul was found guilty.",
                imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_social_share_1200x630_center,f_auto,q_auto:best/mpx/2704722219/2025_05/1748691903616_tdy_sat_chandler_trump_combs_250531_1920x1080-3nqqwy.jpg",
                category = "politics",
                isFeatured = false,
                source = "nbcnews.com",
                publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
            NewsItem(
                uuid = "4cfd6e0c-e546-4887-8d6b-ddd369921017",
                title = "Todd Chrisley Makes First Public Comments After Trump Pardon",
                snippet = "Todd Chrisley is speaking out after he and his wife Julie Chrisley were released from prison following a Trump pardon.",
                imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_social_share_1200x630_center,f_auto,q_auto:best/mpx/2704722219/2025_05/1748691505997_tdy_sat_chandler_todd_chrisley_250531_1920x1080-ojks2j.jpg",
                category = "politics",
                isFeatured = false,
                source = "nbcnews.com",
                publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
            NewsItem(
                uuid = "4efdfbd7-afe8-4351-98ab-1f61b0253aad",
            title = "Biden Says He’s ‘Feeling Good’ in First Comments About Cancer",
            snippet = "Former President Biden appeared at a Memorial Day event in Delaware where he spoke publicly about his Stage 4 prostate cancer diagnosis.",
            imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_social_share_1200x630_center,f_auto,q_auto:best/mpx/2704722219/2025_05/1748691125770_tdy_sat_hillyard_biden_cancer_250531_1920x1080-rnrj4q.jpg",
            category = "politics",
            isFeatured = false,
            source = "nbcnews.com",
            publishedDate = "31-05-2025",
                imageTags = arrayListOf()
            ),
        NewsItem(
            uuid = "01a57cf0-8006-444d-b408-67686452cf15",
            title = "Trump’s crypto complicates Las Vegas wedding between MAGA and bitcoin",
            snippet = "Bitcoin investor Ryan Nichols, who traveled to the Bitcoin 2025 conference from Austin, Texas, said he’s a Trump fan but wouldn’t touch Trump’s digital currency.",
            imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2025-05/250530-donald-trump-bitcoin-ew-338p-6521f0.jpg",
            category = "business",
            isFeatured = false,
            source = "nbcnews.com",
            publishedDate = "31-05-2025",
            imageTags = arrayListOf()
        ),
        NewsItem(
            uuid = "1f1f697d-8794-4186-8755-337a7ec2acba",
            title = "Pakistan Set to Finalise FWBL Privatisation Deal with UAE",
            snippet = "Pakistan is poised to conclude the FWBL privatisation process next month, transferring an 82.64% stake to the United Arab Emirates.",
            imageUrl = "https://www.techjuice.pk/wp-content/uploads/2025/05/pakistan-set-to-finalise-fwbl-privatisation-deal-with-uae-techjuice-177935.jpg",
            category = "tech",
            isFeatured = false,
            source = "techjuice.pk",
            publishedDate = "31-05-2025",
            imageTags = arrayListOf()
        ),
        NewsItem(
            uuid = "1d45d796-dd49-4e0c-b5cf-48ccbd519888",
            title = "Czech Justice Minister Resigns Over Bitcoin Scandal",
            snippet = "Czech Republic’s Justice Minister Pavel Blažek has resigned following mounting pressure over a bitcoin scandal.",
            imageUrl = "https://www.techjuice.pk/wp-content/uploads/2025/05/czech-justice-minister-resigns-over-bitcoin-scandal-techjuice-177931.jpg",
            category = "tech",
            isFeatured = false,
            source = "techjuice.pk",
            publishedDate = "31-05-2025",
            imageTags = arrayListOf()
        ),
        NewsItem(
            uuid = "0c537b8c-c9a7-4528-be20-3a2323c38054",
            title = "Who controls India Inc.? The answer is starting to change: NSE report",
            snippet = "The NSE's March 2025 report reveals private Indian promoters still lead, but domestic mutual funds are catching up.",
            imageUrl = "https://img.etimg.com/thumb/msid-121533879,width-1200,height-630,imgsize-280904,overlay-etmarkets/articleshow.jpg",
            category = "business",
            isFeatured = false,
            source = "economictimes.indiatimes.com",
            publishedDate = "31-05-2025",
            imageTags = arrayListOf()
        )
        )
    }
}
