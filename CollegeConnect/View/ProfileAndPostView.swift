//
//  ProfileAndPostView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

struct ProfileAndPostView: View {
    var body: some View {
        VStack(alignment: .leading){
            SearchBarView()
            Divider()
            HStack{
                Image(systemName: "square.and.pencil")
                Text("Share a post")
            }
            .padding(.horizontal)
            Divider()
            HStack{
                Image(systemName: "photo")
                    .foregroundStyle(Color(.blue))
                    .opacity(0.8)
                Text("Photo")
                Spacer()
                Image(systemName: "video.fill")
                    .foregroundStyle(Color(.green))
                    .opacity(0.8)
                Text("Video")
                Spacer()
                Image(systemName: "calendar")
                    .foregroundStyle(Color(.orange))
                    .opacity(0.8)
                Text("Calendar")
            }
            .padding(.horizontal)
        }
    }
}

#Preview {
    ProfileAndPostView()
}
