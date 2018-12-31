package tv.v1x1.common.util.text;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Josh on 2018-11-15
 */
public class WordList {
    private static final String[] WORD_LIST = new String[] {
            "Optimize", "Prime", "Stone", "Lightning", "Ringer", "Coat", "Kappa", "Invader",
            "Ginger", "Power", "Dans", "Game", "Swift", "Rage", "PJ", "Salt",
            "Sugar", "Kreygasm", "Punch", "Trees", "FrankerZ", "One", "Hand", "Chop",
            "Blood", "Trail", "Asian", "Glow", "Bible", "Thump", "PogChamp", "Hot",
            "Pokket", "Fail", "Fish", "Whole", "Wheat", "RalpherZ", "Kippa", "Keepo",
            "Big", "Brother", "So", "Bayed", "Peoples", "Champ", "Grammar", "King",
            "Panic", "Vis", "Broke", "Back", "Pipe", "Hype", "TheThing", "BabyRage",
            "Basket", "Perma", "Smug", "Buddha", "Bar", "Wut", "Mau5", "HeyGuys",
            "mcaT", "PraiseIt", "Humble", "Life", "Corgi", "Derp", "Cool", "Cat",
            "Face", "NotLikeThis", "Dudu", "Bleed", "Purple", "Raid", "Seems", "Good",
            "Ming", "Lee", "Ross", "OhMyDog", "Frog", "Serious", "Sloth", "Komodo",
            "VoHiYo", "Wealth", "cmonBruh", "NomNom", "Stinky", "Cheese", "Chef", "Frank",
            "Future", "Man", "Vote", "Yea", "Nay", "Rule", "Five", "Drink",
            "Tiny", "Pico", "Mause", "UnSane", "copyThis", "pastaThat", "imGlitch", "GivePLZ",
            "Blarg", "Naut", "Dog", "Jebaited", "Too", "Spicy", "Uncle", "Nox",
            "Racc", "Attack", "Straw", "Beary", "PrimeMe", "Brain", "Slug", "Bat",
            "Chest", "CurseLit", "Poooound", "Super", "Vinlin", "Tri", "Hard", "Story",
            "Bob", "Its", "Boshy", "Time", "KAPOW", "YouDontSay", "UWot", "RlyTho",
            "Sooner", "Later", "Party", "Ninja", "Grumpy", "TheIlluminati", "Bless", "RNG",
            "Morphin", "Thank", "Egg", "Beg", "Phish", "Inuyo", "Kappu", "KonCha",
            "Pun", "Oko", "Saba", "Ping", "Pong", "Tear", "Glove", "Tehe",
            "Pelo", "Lit", "Carl", "Smile", "Crream", "Awk", "Squid", "Unity",
            "Crunchy", "Roll", "Entropy", "Wins", "LUL", "PowerUp", "Dark", "Mode",
            "Twitch", "Votes", "Oopsie", "Woopsie", "Star", "Touchdown", "Pop", "Corn",
            "Tomb", "Earth", "Day", "Hat", "Single", "Ready", "Mercy", "Winged",
            "Pride", "RPG", "Max", "LOL", "Pete", "Zaroll", "Odyssey", "Pixel",
            "Gun", "Run", "Sno", "Fox", "Jacobi", "Carter", "Broken", "Error",
            "B0rkB0rk", "BrokeIt", "V1x1", "V0x1", "Ack", "Alice", "Bad", "Foo",
            "Baz", "Quux", "Borg", "Bug", "Bzzzt", "DEADBEEF", "DeathStar", "FUD",
            "Discord", "SCSI", "Unix", "Linux", "W00t", "Xyzzy", "Mixer", "Bogon",
            "Boink", "Boog", "Bork3n", "Fzzt", "Brick", "0verflow", "Buzz", "Fizz",
            "CamelCase", "Java", "Crypt", "Daemon", "Rezz", "Derezz", "Clue", "Monsta" };

    private static final Random RANDOM = new Random();

    public static String randomWords(final int count) {
        return IntStream.range(0, count)
                .map(x -> RANDOM.nextInt(WORD_LIST.length))
                .mapToObj(x -> WORD_LIST[x])
                .collect(Collectors.joining(""));
    }
}
