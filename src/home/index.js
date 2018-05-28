/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from "react";
import {
  Platform,
  StyleSheet,
  Text,
  View,
  CameraRoll,
  FlatList,
  Image,
  NativeModules,
  Alert,
  ActivityIndicator,
  Dimensions
} from "react-native";

// Create Native Module Variable
const compressImg = NativeModules.CompressImageManager;
let { width, height } = Dimensions.get("window");

export default class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      images: [],
      visible:true
    };
  }

  async componentDidMount() {
    // Get Images from Photo library
    CameraRoll.getPhotos({
      first: 20000000,  // Get first amount of images from Photo Library
      assetType: "All" // Specifies filter on assetType
                      // One of ['All', 'Videos', 'Photos'(default)]
    })
      .then(data => {
        const assets = data.edges;
        const images = assets.map(asset => asset.node.image);
        this.compressImage(images)
      })
      .catch(err => {
        console.log("Error :---->  ", err);
      });
  }

  // Compress Image
  async compressImage(arr) {
    let arrImg = [];
    for (let index = 0; index < arr.length; index++) {
      const item = arr[index];
      const imgResp = await compressImg.fetchPhotos(item.uri);
      arrImg.push(imgResp);
    }
    this.setState({
      images: arrImg
    });
  }

  _renderItem = ({ item }) => (
    <View style={styles.image}>
      <Image style={styles.image} source={{ uri: item }} />
    </View>
  );

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}> Welcome to React Native! </Text>
        <FlatList
          style={{ marginBottom: 20 }}
          data={this.state.images}
          extraData={this.state}
          keyExtractor={(item, index) => index}
          renderItem={this._renderItem}
          numColumns={3}
        />
         {this.state.images.length===0 ? (
          <View style={styles.showActivityView}>
            <ActivityIndicator size="large" color="black" />
          </View>
        ) : null}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#F5FCFF"
  },
  welcome: {
    fontSize: 20,
    textAlign: "center",
    margin: 20
  },
  instructions: {
    textAlign: "center",
    color: "#333333",
    marginBottom: 5
  },
  imageGrid: {
    flex: 1,
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "center"
  },
  image: {
    width: 100,
    height: 100,
    margin: 10
  },
  showActivityView: {
    flex: 1,
    position: 'absolute',
    left: 0,
    top: 0,
    backgroundColor: 'white',
    width: width,
    height: height,
    justifyContent: "center",
    alignItems: "center",
  }
});
