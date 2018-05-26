//
//  CompressImageManager.m
//  DemoImageCompress
//
//  Created by Jay on 16/05/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "CompressImageManager.h"
#import <Photos/Photos.h>

@implementation CompressImageManager

RCT_EXPORT_MODULE()

// Compress Image Method
RCT_EXPORT_METHOD(fetchPhotos:(NSString *)imgURL resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    NSLog(@"local Image URL :====> %@",imgURL);
    CGSize retinaSquare = CGSizeMake(600, 600);
    
    PHImageRequestOptions *cropToSquare = [[PHImageRequestOptions alloc] init];
    cropToSquare.resizeMode = PHImageRequestOptionsResizeModeExact;
    cropToSquare.deliveryMode = PHImageRequestOptionsDeliveryModeOpportunistic;
    [cropToSquare setSynchronous:YES];
    
    NSURL *imageurl = [NSURL URLWithString:imgURL];
    
    PHFetchResult* asset =[PHAsset fetchAssetsWithALAssetURLs:[NSArray arrayWithObjects:imageurl, nil] options:nil];
    
    [[PHImageManager defaultManager] requestImageForAsset:(PHAsset *)[asset objectAtIndex:0] targetSize:retinaSquare contentMode:PHImageContentModeAspectFit                                                options:cropToSquare                                          resultHandler:^(UIImage *fetchedImage, NSDictionary *info) {
      
      //      NSLog(@"Result Image: %@",fetchedImage);
      //      NSLog(@"Result Image info: %@",info);
      
      NSData *imageData = UIImageJPEGRepresentation(fetchedImage,0.65);
      
      NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
      NSTimeInterval timeStamp = [[NSDate date] timeIntervalSince1970];
      NSString *filePath = [[paths objectAtIndex:0] stringByAppendingPathComponent:[NSString stringWithFormat:@"%0.0f.jpg", timeStamp*1000]];
      NSError *error = nil;
      [imageData writeToFile:filePath options:NSDataWritingAtomic error:&error];
      NSURL* fileUrl = [NSURL fileURLWithPath:filePath];
      if(error){
        fileUrl = imageurl;
      }
      NSLog(@"Compress Image URL :====> %@",fileUrl);
      NSString *str = [NSString stringWithFormat:@"%@",fileUrl];
      resolve(str);
    }];
}

@end
