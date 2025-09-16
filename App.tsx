import {GlamAr, GlamArProvider} from '@glamario/core-react-native';
import React, {useEffect} from 'react';
import {SafeAreaView, Button, View, StyleSheet} from 'react-native';

export default function App() {
  useEffect(() => {
    // 1) Init once
    GlamAr.init({
      apiKey: 'Your_API_Key',
      platform: 'react_native',
      category: 'skinanalysis',
      configuration: {
        skinAnalysis: {
          appId: 'Your_Skinanalysis_app_ID',
        },
      },
    });

    // 2) Example event listener
    const photoLoadedSubscription = GlamAr.on('photo-loaded', (data: any) => {
      console.log('Photo loaded:', data);
    });

    const loadedSubscription = GlamAr.on('loaded', (data: any) => {
      console.log('glamar loaded', data);
    });

    // 3) Cleanup on unmount
    return () => {
      // NativeEventEmitter returns an EmitterSubscription with remove()
      photoLoadedSubscription?.remove?.();
      loadedSubscription?.remove?.();
    };
  }, []);

  const handleApply = () => {
    GlamAr.applyByCategory('sunglasses');
  };

  const handleSnapshot = () => {
    GlamAr.snapshot();
  };

  const handleReset = () => {
    GlamAr.reset();
  };

  return (
    <SafeAreaView style={styles.container}>
      {/* Full AR surface */}
      <View style={styles.glamAR}>
        <GlamArProvider />
      </View>

      {/* Overlay controls at the bottom */}
      <View style={styles.controls}>
        <Button title="Apply Sunglasses" onPress={handleApply} />
        <Button title="Take Snapshot" onPress={handleSnapshot} />
        <Button title="Reset" onPress={handleReset} />
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  glamAR: {
    width: '100%',
    height: '100%',
    zIndex: 0,
  },
  controls: {
    position: 'absolute',
    bottom: 20, // distance from bottom of the screen
    left: 0,
    right: 0,
    flexDirection: 'row',
    justifyContent: 'space-evenly', // space out buttons evenly in the row
    paddingHorizontal: 20,
    zIndex: 100,
  },
});
