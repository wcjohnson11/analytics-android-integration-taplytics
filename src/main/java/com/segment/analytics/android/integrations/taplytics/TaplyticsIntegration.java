package com.segment.analytics.android.integrations.taplytics;

import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;
import com.segment.analytics.ValueMap;
import com.segment.analytics.integrations.Integration;
import com.segment.analytics.integrations.Logger;
import com.segment.analytics.integrations.TrackPayload;
import com.taplytics.sdk.Taplytics;
import org.json.JSONObject;

/**
 * Created by williamjohnson on 4/19/16.
 */
public class TaplyticsIntegration extends Integration<Taplytics> {
    public static final Factory FACTORY = new Factory() {
        @Override public Integration<?> create(ValueMap settings, Analytics analytics) {
            return new TaplyticsIntegration(analytics, settings);
        }

        @Override public String key() {
            return TAPLYTICS_KEY;
        }
    };
    private static final String TAPLYTICS_KEY = "Taplytics";
    final Logger logger;
    String apiKey;
    boolean liveUpdate;
    int sessionMinutes;

    TaplyticsIntegration(Analytics analytics, ValueMap settings){
      logger = analytics.logger(TAPLYTICS_KEY);

      String apiKey = settings.getString("apiKey");
      liveUpdate = settings.getBoolean("liveUpdate", true);
      sessionMinutes = settings.getInt("sessionMinutes", 10);

      Taplytics.startTaplytics(analytics.getApplication(), apiKey);
      logger.verbose("Taplytics.startTaplytics(analytics.getApplication(), %s)", apiKey);
    }

    @Override public void track(TrackPayload track) {
      String event = track.event();

      event(event, track.properties());
    }

  private void event(String name, Properties properties) {
    JSONObject propertiesJSON = properties.toJsonObject();
    Taplytics.logEvent(name, properties.value(), propertiesJSON);
    // if revenue, logRevenue
  }
};
