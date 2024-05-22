//
//  PostCardView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

struct SocialView {
    var ids: Int
    var image: String
    var title: String
}

var socialView: [SocialView] = [
    .init(ids: 0, image: "hand.thumbsup", title: "Like"),
    .init(ids: 1, image: "text.bubble", title: "Comment"),
    .init(ids: 2, image: "arrow.turn.up.right", title: "Share"),
    .init(ids: 4, image: "paperplane.fill", title: "Send")
]

let samplePostData = PostModel(id: 0, image: "0", title: "SDE at Amazon", followers: 10, profileImage: "01",  post: "Offer from Amazon", time: "8m")

struct PostCardView: View {
    var Data: PostModel
    var body: some View {
        VStack(alignment: .leading){
            Rectangle()
                .fill(.gray.opacity(0.4))
                .frame(width: .infinity, height: 8)
                .ignoresSafeArea(.all)
            HStack(alignment: .center){
                Image(Data.profileImage)
                    .resizable()
                    .clipShape(Circle())
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 80, height: 80, alignment: .leading)
                VStack(alignment: .leading){
                    Text(Data.title)
                        .font(.body)
                    Text("\(Data.followers) followers")
                        .font(.subheadline)
                        .foregroundStyle(Color(.gray))
                    Text(Data.time)
                        .font(.caption)
                        .foregroundStyle(Color(.gray))
                }
                Spacer()
                Image(systemName: "ellipsis")
            }
            .padding(.horizontal)
            Text(Data.post)
                .padding(.horizontal)
            Image(Data.image)
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: .infinity, height: .infinity, alignment: .center)
            Divider()
        }
    }
}

#Preview {
    PostCardView(Data: samplePostData)
}
