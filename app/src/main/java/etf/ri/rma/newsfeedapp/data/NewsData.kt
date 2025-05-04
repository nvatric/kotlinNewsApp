package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem
import java.util.UUID

object NewsData {
    fun getAllNews(): List<NewsItem>{
        return listOf(
            NewsItem(
                id = "0",
                title = "Digitalizacija javne uprave do kraja godine",
                snippet = "Građani će moći podnositi zahtjeve online.",
                imageUrl = null,
                category = "Politika",
                isFeatured = false,
                source = "Avaz",
                publishedDate = "10-04-2025"
            ),
            NewsItem(
                id = "1",
                title = "BiH otvara pregovore s EU",
                snippet = "Veliki dan za evropsku integraciju.",
                imageUrl = null,
                category = "Politika",
                isFeatured = true,
                source = "N1",
                publishedDate = "08-04-2025"
            ),
            NewsItem(
                id = "2",
                title = "Novi budžet predviđa veća ulaganja u obrazovanje",
                snippet = "Planiraju se veća sredstva za škole i univerzitete.",
                imageUrl = null,
                category = "Politika",
                isFeatured = false,
                source = "Oslobođenje",
                publishedDate = "07-04-2025"
            ),
            NewsItem(
                id = "3",
                title = "Parlament raspravlja o novim zakonima za mlade",
                snippet = "Poticaji za zapošljavanje i obrazovanje.",
                imageUrl = null,
                category = "Politika",
                isFeatured = true,
                source = "Nezavisne novine",
                publishedDate = "06-04-2025"
            ),
            NewsItem(
                id = "4",
                title = "Ministar sigurnosti upozorava na cyber prijetnje",
                snippet = "Poziv na jačanje zaštite IT sistema.",
                imageUrl = null,
                category = "Politika",
                isFeatured = false,
                source = "RTRS",
                publishedDate = "05-04-2025"
            ),
            NewsItem(
                id = "5",
                title = "Predsjedništvo BiH na samitu u Beču",
                snippet = "Ekonomska saradnja u fokusu sastanka.",
                imageUrl = null,
                category = "Politika",
                isFeatured = true,
                source = "Al Jazeera Balkans",
                publishedDate = "04-04-2025"
            ),
            NewsItem(
                id = "6",
                title = "FK Sarajevo izborio finale Kupa BiH",
                snippet = "Pobjedom protiv Širokog Brijega osigurali plasman u veliko finale.",
                imageUrl = null,
                category = "Sport",
                isFeatured = false,
                source = "Sportske.ba",
                publishedDate = "11-04-2025"
            ),
            NewsItem(
                id = "7",
                title = "Zmajevi spremni za meč protiv Italije",
                snippet = "Selektor kaže: 'Ovo je naša šansa!'",
                imageUrl = null,
                category = "Sport",
                isFeatured = true,
                source = "SportSport.ba",
                publishedDate = "10-04-2025"
            ),

            NewsItem(
                id = "8",
                title = "Sarajevski maraton oborio rekorde",
                snippet = "Preko 12.000 učesnika iz 40 zemalja.",
                imageUrl = null,
                category = "Sport",
                isFeatured = false,
                source = "Avaz",
                publishedDate = "09-04-2025"
            ),
            NewsItem(
                id = "9",
                title = "Mladi teniser iz Tuzle pobijedio na turniru u Rimu",
                snippet = "Talent koji obećava veliku karijeru.",
                imageUrl = null,
                category = "Sport",
                isFeatured = true,
                source = "Klix.ba",
                publishedDate = "08-04-2025"
            ),
            NewsItem(
                id = "10",
                title = "Košarkašice BiH plasirale se na EP",
                snippet = "Dramatična završnica protiv Češke.",
                imageUrl = null,
                category = "Sport",
                isFeatured = false,
                source = "N1",
                publishedDate = "07-04-2025"
            ),
            NewsItem(
                id = "11",
                title = "Banjalučki klub osvaja titulu",
                snippet = "Fudbalski klasik odlučen penalima.",
                imageUrl = null,
                category = "Sport",
                isFeatured = true,
                source = "RTRS",
                publishedDate = "06-04-2025"
            ),
            NewsItem(
                id = "12",
                title = "eSports ekipa iz Sarajeva među top 10 u Evropi",
                snippet = "Ogroman uspjeh domaće gaming scene.",
                imageUrl = null,
                category = "Sport",
                isFeatured = false,
                source = "Oslobođenje",
                publishedDate = "05-04-2025"
            ),
            NewsItem(
                id = "13",
                title = "Bosanskohercegovački naučnici razvili biorazgradivu plastiku",
                snippet = "Revolucionarno otkriće u borbi protiv zagađenja.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = true,
                source = "Al Jazeera Balkans",
                publishedDate = "10-04-2025"
            ),
            NewsItem(
                id = "14",
                title = "Studenti ETF-a konstruisali autonomnog robota",
                snippet = "Robot koji se može snalaziti u lavirintima.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = false,
                source = "Klix.ba",
                publishedDate = "09-04-2025"
            ),
            NewsItem(
                id = "15",
                title = "Prva bh. kompanija lansira nanosatelit",
                snippet = "Projekat u saradnji sa evropskom svemirskom agencijom.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = true,
                source = "N1",
                publishedDate = "08-04-2025"
            ),
            NewsItem(
                id = "16",
                title = "AI startup iz BiH privukao milionsku investiciju",
                snippet = "Algoritam koji predviđa bolesti u ranoj fazi.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = false,
                source = "Avaz",
                publishedDate = "07-04-2025"
            ),
            NewsItem(
                id = "17",
                title = "Otvorena nova STEM laboratorija za djecu",
                snippet = "Podsticanje radoznalosti od najranijih godina.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = true,
                source = "Nezavisne novine",
                publishedDate = "06-04-2025"
            ),
            NewsItem(
                id = "18",
                title = "Inovacija iz BiH testira se u Švicarskoj",
                snippet = "Pametna narukvica za mjerenje vitalnih funkcija.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = false,
                source = "Oslobođenje",
                publishedDate = "05-04-2025"
            ),
            NewsItem(
                id = "19",
                title = "Sarajevski studenti razvili aplikaciju za učenje uz pomoć AI",
                snippet = "Nova aplikacija koristi vještačku inteligenciju za personalizirano učenje.",
                imageUrl = null,
                category = "Nauka/tehnologija",
                isFeatured = true,
                source = "Klix.ba",
                publishedDate = "10-04-2025"
            )
        )
    }
}