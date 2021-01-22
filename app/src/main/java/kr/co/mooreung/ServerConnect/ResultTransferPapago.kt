package kr.co.mooreung.ServerConnect

data class ResultTransferPapago (
    var message: Message
)

data class Message(
    var result: Result
)

data class Result (
    var srcLangType: String = "",
    var tarLangType: String = "",
    var translatedText: String = ""
)