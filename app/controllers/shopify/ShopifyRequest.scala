package controllers.shopify

import controllers.ApplicationLogging
import io.lemonlabs.uri.QueryString
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.io.Codec

case class ShopifyRequest(query: String, hmac: String, shop: String, timestamp: String) extends ApplicationLogging {

  lazy val queryWithoutHmac: String = QueryString.parse(query)
    .removeAll(ShopifyRequest.HmacKey).toString
    .replace("?", "")


  def validate(): Boolean = {
    logger.info(ShopifySecrets.Default.toString)
    val scs = new SecretKeySpec(Codec.toUTF8(ShopifySecrets.Default.apiSecret.toCharArray), ShopifyRequest.Algorithm)
    val mac = Mac.getInstance(ShopifyRequest.Algorithm)

    mac.init(scs)
    val output = mac.doFinal(Codec.toUTF8(queryWithoutHmac.toCharArray))

    logger.info("Calculated HMAC")
    logger.info(output.toString)

    logger.info("Given HMAC")
    logger.info(hmac)
    true
  }
}

object ShopifyRequest {
  val Algorithm = "HmacSHA256"

  val HmacKey = "hmac"
  val ShopKey = "shop"
  val TimestampKey = "timestamp"

  def apply(query: String): Option[ShopifyRequest] = for {
    parsedQuery <- QueryString.parseOption(query)
    parsedHmac <- parsedQuery.param(HmacKey)
    parsedShop <- parsedQuery.param(ShopKey)
    parsedTimestamp <- parsedQuery.param(TimestampKey)
  } yield ShopifyRequest(query, parsedHmac, parsedShop, parsedTimestamp)
}