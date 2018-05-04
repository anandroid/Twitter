package appengine.parser.optimal.constants;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExcludeList {

    static HashMap<String, List<Market>> exclusions = new HashMap<>();

    public static boolean isExcluded(String coin, CoinMarket firstMarket, CoinMarket secondMarket) {
        if (exclusions.size() == 0) {
            exclusions.put("PXC", Arrays.asList(Market.CRYPTOPIA, Market.BITZ));
            exclusions.put("SMT", Arrays.asList(Market.SOUTHXCHANGE));
            exclusions.put("OC", Arrays.asList(Market.BITZ, Market.COINEXCHANGE));
            exclusions.put("BTM", Arrays.asList(Market.POLONEIX));
            exclusions.put("SBD", Arrays.asList(Market.HitBTC));
            exclusions.put("WRC", Arrays.asList(Market.CRYPTOPIA, Market.COINEXCHANGE));
            exclusions.put("POSW", Arrays.asList(Market.CRYPTOPIA));
            exclusions.put("DFS", Arrays.asList(Market.CRYPTOPIA));
            exclusions.put("BTA", Arrays.asList(Market.SOUTHXCHANGE));
            exclusions.put("GOLOS", Arrays.asList(Market.LIQUI));
            exclusions.put("INCNT",Arrays.asList(Market.LIQUI));
        }

        List<Market> markets = exclusions.get(coin.toUpperCase());
        if (markets != null && markets.size() > 0) {
            if (markets.size() == 2 && markets.contains(firstMarket.getMarket()) && (secondMarket != null && markets.contains(secondMarket.getMarket()))) {
                return true;
            }

            if (markets.size() == 1 && (markets.contains(firstMarket.getMarket()) || (secondMarket != null && markets.contains(secondMarket.getMarket())))) {
                return true;
            }
        }

        return false;
    }


}
