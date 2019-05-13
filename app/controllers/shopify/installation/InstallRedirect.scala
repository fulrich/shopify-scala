package controllers.shopify.installation

import java.util.UUID

import controllers.shopify.ShopifySecrets
import io.lemonlabs.uri.Url


case class InstallRedirect(
  uri: String,
  nonce: String
)

object InstallRedirect {
  val AuthorizePath = "/admin/oauth/authorize"

  def apply(parameters: InstallParameters, redirectUri: String, nonce: String = UUID.randomUUID.toString): InstallRedirect = {
    val urlBuilder = Url(scheme = "https", host = parameters.shop, path = AuthorizePath)
      .addParam("client_id" -> ShopifySecrets.Default.apiKey)
      .addParam("scopes" -> "write_orders,read_customers")
      .addParam("redirect_uri" -> ("https://scalify.heroku.com" + redirectUri))
      .addParam("nonce" -> nonce)
      .addParam("access_mode" -> "offline")

    InstallRedirect(urlBuilder.toString, nonce)
  }
}
