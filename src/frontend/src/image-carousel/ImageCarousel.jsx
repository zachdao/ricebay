import React, { useState } from 'react';
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import {
    AlertDialog,
    DialogContainer,
    Image,
    View,
} from '@adobe/react-spectrum';

const responsive = {
    desktop: {
        breakpoint: { max: 3000, min: 1024 },
        items: 3,
        slidesToSlide: 3, // optional, default to 1.
    },
    tablet: {
        breakpoint: { max: 1024, min: 464 },
        items: 2,
        slidesToSlide: 2, // optional, default to 1.
    },
    mobile: {
        breakpoint: { max: 464, min: 0 },
        items: 1,
        slidesToSlide: 1, // optional, default to 1.
    },
};

export const ImageCarousel = ({ images, setImages }) => {
    const [open, setOpen] = useState();
    return (
        <View
            width="100%"
            height="100%"
            maxWidth="550px"
            maxHeight="550px"
            overflow="hidden"
        >
            <Carousel
                responsive={responsive}
                showDots={true}
                autoPlay={false}
                keyBoardControl={true}
            >
                {images.map((image, i) => (
                    <View key={i} margin="size-100">
                        <div onClick={() => setOpen(i)}>
                            <Image src={image} />
                        </div>
                        <DialogContainer onDismiss={() => setOpen(undefined)}>
                            {open === i && setImages && (
                                <AlertDialog
                                    title="Remove Image"
                                    primaryActionLabel="Delete"
                                    variant="destructive"
                                    cancelLabel="Cancel"
                                    onPrimaryAction={() => {
                                        const copy = [...images];
                                        copy.splice(i, 1);
                                        setImages(copy);
                                    }}
                                >
                                    <Image src={image} />
                                </AlertDialog>
                            )}
                        </DialogContainer>
                    </View>
                ))}
            </Carousel>
        </View>
    );
};
