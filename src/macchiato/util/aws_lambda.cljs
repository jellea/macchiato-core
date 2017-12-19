(ns macchiato.util.aws-lambda
  (:require [clojure.string :as str]))

(defn request->map [request ctx]
  (let [req (.-proxyRequest request)
        ;;conn         (.-connection req)
        ;;url          (.parse url-parser (.-url req))
        ;;NOTE: do I need to parse url? prob not
        ;;http-version (.-httpVersion req)
        headers      (js->clj (.-headers req) :keywordize-keys true)
        ;;address      (js->clj (.address conn) :keywordize-keys true)

        out  {;;:server-port     (:port address)
              ;;:server-name     (:address address)
              ;;:remote-addr     (.-remoteAddress conn)
              :headers         headers
              ;;:cookies         (cookies/request-cookies req res (:cookies opts))
              ;;:content-type    (get headers "content-type")
              ;;:content-length  (get headers "content-length")
              :request-method  (keyword (str/lower-case (.-httpMethod req)))
              :url             (.-path req)
              :uri             (.-path req)
              ;;:query-string    (when-let [query (.-search url)] (.substring query 1))
              :query-params    (js->clj (.-queryStringParameters req))
              :body            (.-body req)}]
              ;;:fresh?          (.-fresh req)
              ;;:hostname        (-> req .-headers .-host (str/split #":") first)
              ;;:params          (js->clj (.-params req))
              ;;:protocol        (str (if (= :http scheme) "HTTP/" "HTTPS/") http-version)
              ;;:secure?         (.-secure req)
              ;;:signed-cookies  (js->clj (.-signedCookies req))
              ;;:ssl-client-cert (when-let [peer-cert-fn (.-getPeerCertificate conn)] (peer-cert-fn))
              ;;:stale?          (.-state req)
              ;;:subdomains      (js->clj (.-subdomains req))
              ;;:xhr?            (.-xhr req)
              ;;:scheme          scheme
              ;;:node/request    req
              ;;:node/response   res
    out))

(defn response->map [{:keys [body] :as r}]
  (clj->js (assoc r :statusCode (:status r)
                    :body (if (string? body) body (-> body clj->js (js/JSON.stringify)))
                    :isBase64Encoded false)))

(defn response [cb request-map node-server-response raise opts]
  (cb nil (response->map request-map)))

(defn raise [cb node-server-response]
  ;;TODO: 500 status
  (cb "broken" nil))