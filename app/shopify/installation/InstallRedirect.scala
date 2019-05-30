package shopify.installation

import java.util.UUID

import shopify.ShopifyConfiguration
import io.lemonlabs.uri.Url


case class InstallRedirect(
  shop: String,
  redirectUri: String,
  scopes: Seq[String] = Vector.empty,
  nonce: String = UUID.randomUUID.toString)(implicit shopifySecrets: ShopifyConfiguration) {

  lazy val uri: String =
    Url(scheme = InstallRedirect.Protocol, host = shop, path = InstallRedirect.AuthorizePath)
      .addParam("client_id" -> shopifySecrets.apiKey)
      .addParam("scope" -> scopes.mkString(","))
      .addParam("redirect_uri" -> redirectUri)
      .addParam("nonce" -> nonce)
      .toString
}

object InstallRedirect {
  val Protocol = "https"
  val AuthorizePath = "/admin/oauth/authorize"

  def apply(redirectUri: String)(parameters: InstallParameters)(implicit shopifySecrets: ShopifyConfiguration): InstallRedirect =
    apply(parameters, redirectUri)

  def apply(parameters: InstallParameters, redirectUri: String)(implicit shopifySecrets: ShopifyConfiguration): InstallRedirect =
    InstallRedirect(parameters.shop, redirectUri)
}
