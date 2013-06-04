
fun int_to_string n =
  if n < 0 then
    "-" ^ Int.toString (~n)
  else
    Int.toString n

fun XML_stats stats =
let
  fun void property content =
    "  <void property=\"" ^ property ^ "\">\n" ^
    content ^
    "  </void>\n"
  fun wrap tag content =
    "   <" ^ tag ^ ">" ^ content ^ "</" ^ tag ^ ">\n"
  fun long int =
    wrap "long" (int_to_string int)
  fun double time =
    wrap "double" (Time.toString time)
  fun void_index_long index int =
    "    <void index=\"" ^ int_to_string index ^ "\">\n" ^
    "  " ^ long int ^
    "    </void>\n"
  fun array vector =
    "   <array class=\"long\" length=\"" ^ (int_to_string (Vector.length vector)) ^ "\">\n" ^
    Vector.foldli (fn (i, c, s) => s ^ void_index_long i c) "" vector ^
    "   </array>\n"
  val {sizeHeap: int,
       gcFullGCs: int,
       timeGCUser: Time.time,
       threadsInML: int,
       gcPartialGCs: int,
       threadsTotal: int,
       timeGCSystem: Time.time,
       userCounters: int vector,
       threadsWaitIO: int,
       timeNonGCUser: Time.time,
       sizeAllocation: int,
       timeNonGCSystem: Time.time,
       threadsWaitMutex: int,
       threadsWaitSignal: int,
       sizeAllocationFree: int,
       sizeHeapFreeLastGC: int,
       threadsWaitCondVar: int,
       sizeHeapFreeLastFullGC: int,
       ...} = stats
in
  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" ^
  "<java version=\"1.7.0_09-icedtea\" class=\"java.beans.XMLDecoder\">\n" ^
  " <object class=\"com.model.PolyMLStatistics\">\n" ^
  void "gcFullGCs" (long gcFullGCs) ^
  void "gcPartialGCs" (long gcPartialGCs) ^
  void "sizeAllocation" (long sizeAllocation) ^
  void "sizeAllocationFree" (long sizeAllocationFree) ^
  void "sizeHeap" (long sizeHeap) ^
  void "sizeHeapFreeLastFullGC" (long sizeHeapFreeLastFullGC) ^
  void "sizeHeapFreeLastGC" (long sizeHeapFreeLastGC) ^
  void "threadsInML" (long threadsInML) ^
  void "threadsTotal" (long threadsTotal) ^
  void "threadsWaitCondVar" (long threadsWaitCondVar) ^
  void "threadsWaitIO" (long threadsWaitIO) ^
  void "threadsWaitMutex" (long threadsWaitMutex) ^
  void "threadsWaitSignal" (long threadsWaitSignal) ^
  void "timeGCSystem" (double timeGCSystem) ^
  void "timeGCUser" (double timeGCUser) ^
  void "timeNonGCSystem" (double timeNonGCSystem) ^
  void "timeNonGCUser" (double timeNonGCUser) ^
  void "userCounters" (array userCounters) ^
  " </object>\n" ^
  "</java>\n"
end

fun stats_or_fail n =
  XML_stats (PolyML.Statistics.getRemoteStats n)
    handle Fail _ => ""

fun wrap_poly s =
  "<polymlresponse>\n" ^ s ^ "</polymlresponse>\n"

fun main () : unit =
  (case TextIO.inputLine TextIO.stdIn of
    SOME "\n" => main ()
  | SOME line =>
      (case Int.fromString line of
        SOME n => (print (wrap_poly (stats_or_fail n));
                   main ())
      | NONE => OS.Process.exit OS.Process.success)
  | NONE => OS.Process.exit OS.Process.success)
  handle Size => OS.Process.exit OS.Process.success;
