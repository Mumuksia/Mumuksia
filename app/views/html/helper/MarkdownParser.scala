package views.html.helper

import play.api.templates.Html

/**
 * Author: mange
 * Created: 2012-04-09
 */

package object MarkdownParser {

  def parseMarkdown(string:String) = {
    Html(string)
  }

}