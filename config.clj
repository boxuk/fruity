
(def config {
    :libraries [
        { :name "obscura"
          :url "http://github.com/boxuk/obscura"
          :type :git
          :packageCommand "phing pear-package -D=$VERSION" }
        { :name "routing"
          :url "http://github.com/boxuk/boxuk-routing"
          :type :git
          :packageCommand "phing pear-package -D=$VERSION" }
    ]
})

