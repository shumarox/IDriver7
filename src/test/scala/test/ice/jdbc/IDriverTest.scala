package test.ice.jdbc

import java.io.{BufferedWriter, FileWriter}
import java.sql.DriverManager
import java.util.Properties

import ice.jdbc.{IDriver, IExtraWorkerHandler, StandardLogWorker}
import org.junit.Test

import scala.util.matching.Regex

class IDriverTest {

  @Test
  def test(): Unit = {
    // テストを同時実行させないための同期化
    IExtraWorkerHandler.synchronized {
      val writer = new BufferedWriter(new FileWriter("./IDriverTest.log", true))

      try {
        // 標準ログワーカーをカスタマイズして登録
        IExtraWorkerHandler.extraWorker = new StandardLogWorker {
          private val skipClassNameRegex: Regex = """^(java\.|scala\.|ice\.jdbc\.).*""".r

          override def isSkipClassName(s: String): Boolean = skipClassNameRegex.findFirstIn(s).nonEmpty

          override def write(s: String): Unit = {
            writer.write(s + "\n")
            writer.flush()
          }
        }

        // IDriverの登録（テストを繰り返し実行する場合に必要）
        // ※META-INF/java.sql.Driverに記載しているため不要
        new IDriver()

        Class.forName("test.ice.jdbc.DDriver$")

        val url = "i!jdbc:test://testhost/testdb"
        val props = new Properties {
          setProperty("user", "test_user")
          setProperty("password", "himitsu")
        }

        val con = DriverManager.getConnection(url, props)

        try {
          val ps = con.prepareStatement("SELECT * FROM TEST\nWHERE X IN (?, ?)")

          try {
            ps.setString(1, "foo")
            ps.setString(2, "var")

            val rs = ps.executeQuery()

            try {
              while (rs.next()) println("result: " + (rs.getString(1), rs.getString(2)))
            } finally {
              rs.close()
            }
          } finally {
            ps.close()
          }

          con.commit();
        } finally{
          con.close()
        }
      } finally {
        writer.close()
      }
    }
  }
}
