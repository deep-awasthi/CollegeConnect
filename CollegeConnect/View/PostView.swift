//
//  PostView.swift
//  CollegeConnect
//
//  Created by Deep Awasthi on 22/5/24.
//

import SwiftUI

var postModel: [PostModel] = [
    .init(id: 0, image: "0", title: "SDE at Amazon", followers: 4066, profileImage: "00", post: "learning iOS", time: "8m"),
    .init(id: 1, image: "1", title: "Analyst at Deloitte", followers: 2345, profileImage: "01", post: "SDE Offer", time: "8m"),
    .init(id: 2, image: "2", title: "MTS at Adobe", followers: 1235, profileImage: "02", post: "Exam Schedule", time: "8m"),
    .init(id: 3, image: "3", title: "SDE at Uber", followers: 908, profileImage: "03", post: "Acheivement Unlocked", time: "8m"),
    .init(id: 4, image: "4", title: "Engineer at Intel", followers: 1234, profileImage: "04", post: "Refferal Available", time: "8m"),
    .init(id: 5, image: "5", title: "SDE at Paytm", followers: 567, profileImage: "05", post: "Help Required", time: "8m"),
    .init(id: 6, image: "6", title: "Educator at PW", followers: 346, profileImage: "00", post: "New Course in Youtube", time: "8m")
    
    ]

struct PostView: View {
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            ForEach(postModel, id: \.id){ post in
                PostCardView(Data: post)
                HStack(alignment: .center, spacing: 45){
                    ForEach(socialView, id: \.ids){data in
                        VStack{
                            Image(systemName: data.image)
                            Text("\(data.title)")
                                .multilineTextAlignment(/*@START_MENU_TOKEN@*/.leading/*@END_MENU_TOKEN@*/)
                        }
                        .foregroundColor(.black.opacity(0.8))
                        .font(.subheadline)
                    }
                }
            }
        }
    }
}

#Preview {
    PostView()
}
