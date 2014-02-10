import models.User
import org.junit._
import org.junit.Assert._
import play.test._
import play.db.jpa.JPA
/*import models._*/
import play.db.ebean._

class BasicTest {

  @Test
  def aVeryImportantThingToTest() {

    assertEquals(2, 1 + 1)
  }

  @Test
  def createAndRetrieveUser() {
    new User("yurrko@gmail.com", "yurko", "yurko").save()
    val bob = find[User]("byFullName", "yurko").first
    assertNotNull(bob)
    assertEquals("yurrko@gmail.com", bob.email)
  }

}